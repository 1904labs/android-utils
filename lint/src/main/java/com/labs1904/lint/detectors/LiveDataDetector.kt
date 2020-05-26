package com.labs1904.lint.detectors

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UClass

@Suppress("UnstableApiUsage")
class LiveDataDetector : Detector(), Detector.UastScanner {

	override fun getApplicableUastTypes() = listOf(UClass::class.java)

	override fun createUastHandler(context: JavaContext): UElementHandler? =
		object : UElementHandler() {
			override fun visitClass(node: UClass) {

				node.fields
					.filter { it.type.canonicalText.contains("androidx.lifecycle.LiveData") }
					.forEach { field ->
						val lintFix = LintFix.create()
							.name("Replace with KotlinLiveData")
							.replace()
							.text("LiveData")
							.with("KotlinLiveData")
							.autoFix()
							.build()

						field.typeReference?.let { type ->
							context.getLocation(type)
						}?.let { typeLocation ->
							context.report(
								ISSUE,
								field,
								typeLocation,
								"LiveData should be replaced by KotlinLiveData",
								lintFix
							)
						}
					}
			}
		}

	companion object {
		private val IMPLEMENTATION = Implementation(
			LiveDataDetector::class.java,
			Scope.JAVA_FILE_SCOPE
		)

		val ISSUE by lazy {
			Issue.create(
				id = "LiveDataUsed",
				briefDescription = "LiveData is being used instead of KotlinLiveData",
				explanation = "KotlinLiveData should be used instead of LiveData for null safety",
				category = Category.CORRECTNESS,
				severity = Severity.ERROR,
				implementation = IMPLEMENTATION
			)
		}
	}
}
