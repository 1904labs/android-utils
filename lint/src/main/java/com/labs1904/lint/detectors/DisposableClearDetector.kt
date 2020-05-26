package com.labs1904.lint.detectors

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UField
import org.jetbrains.uast.visitor.AbstractUastVisitor

@Suppress("UnstableApiUsage")
class DisposableClearDetector : Detector(), Detector.UastScanner {

	override fun getApplicableUastTypes() = listOf(UClass::class.java)

	override fun createUastHandler(context: JavaContext): UElementHandler? = object : UElementHandler() {
		override fun visitClass(node: UClass) {
			node.fields
				.filter { "io.reactivex.disposables.Disposable" == it.type.canonicalText }
				.toMutableList()
				.let { disposables ->
					node.accept(expressionVisitor(disposables))

					disposables.forEach {
						context.report(
							ISSUE,
							it,
							context.getNameLocation(it),
							"Disposable.dispose() has not been called"
						)
					}
				}
		}
	}

	private fun expressionVisitor(disposables: MutableList<UField>) =
		object : AbstractUastVisitor() {
			override fun visitCallExpression(node: UCallExpression): Boolean {
				return when (node.methodName) {
					"dispose" -> {
						disposables.iterator().let {
							while (it.hasNext()) {
								if (node.receiver?.asRenderString() == it.next().name) {
									it.remove()
								}
							}
						}

						true
					}
					else -> super.visitCallExpression(node)
				}
			}
		}

	companion object {
		private val IMPLEMENTATION = Implementation(
			DisposableClearDetector::class.java,
			Scope.JAVA_FILE_SCOPE
		)

		val ISSUE by lazy {
			Issue.create(
				id = "DisposableNotDisposed",
				briefDescription = "Disposable has not been disposed",
				explanation = "A Disposable reference is being held, but it is not being disposed of.",
				category = Category.CORRECTNESS,
				severity = Severity.ERROR,
				implementation = IMPLEMENTATION
			)
		}
	}
}
