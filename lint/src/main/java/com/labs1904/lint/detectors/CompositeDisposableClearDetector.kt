package com.labs1904.lint.detectors

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UField
import org.jetbrains.uast.visitor.AbstractUastVisitor

@Suppress("UnstableApiUsage")
class CompositeDisposableClearDetector : Detector(), Detector.UastScanner {

	override fun getApplicableUastTypes(): List<Class<out UElement>>? = listOf(UClass::class.java)

	override fun createUastHandler(context: JavaContext): UElementHandler? =
		object : UElementHandler() {
			override fun visitClass(node: UClass) {
				node.fields
					.filter {
						"io.reactivex.disposables.CompositeDisposable" == it.type.canonicalText
					}
					.toMutableList()
					.let { compositeDisposables ->
						node.accept(expressionVisitor(compositeDisposables))

						compositeDisposables.forEach {
							context.report(
								ISSUE,
								it,
								context.getNameLocation(it),
								"CompositeDisposable.clear() has not been called"
							)
						}
					}
			}
		}

	private fun expressionVisitor(compositeDisposables: MutableList<UField>) =
		object : AbstractUastVisitor() {
			override fun visitCallExpression(node: UCallExpression): Boolean {
				return when (node.methodName) {
					"clear" -> {
						compositeDisposables.iterator().let {
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
			CompositeDisposableClearDetector::class.java,
			Scope.JAVA_FILE_SCOPE
		)

		val ISSUE: Issue by lazy {
			Issue.create(
				id = "CompositeDisposableClear",
				briefDescription = "CompositeDisposable not cleared",
				explanation = "A CompositeDisposable is being used, but it is not being cleared. " +
					"This can leave subscriptions active longer than the life of an Activity/Fragment",
				category = Category.CORRECTNESS,
				severity = Severity.ERROR,
				implementation = IMPLEMENTATION
			)
		}
	}
}
