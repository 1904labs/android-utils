package com.labs1904.lint.detectors

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UClass

@Suppress("UnstableApiUsage")
class MutableLiveDataDetector : Detector(), Detector.UastScanner {

	override fun getApplicableUastTypes() = listOf(UClass::class.java)

	override fun createUastHandler(context: JavaContext): UElementHandler? =
		object : UElementHandler() {
			override fun visitClass(node: UClass) {
				node.fields
					.filter { it.type.canonicalText.contains("androidx.lifecycle.MutableLiveData") }
					.forEach { field ->
						val lintFix =
							LintFix.create()
								.name("Replace with KotlinMutableLiveData")
								.replace()
								.text("MutableLiveData")
								.with("KotlinMutableLiveData")
								.autoFix()
								.build()

						field.typeReference?.let {
							context.getLocation(it)
						}?.let { typeLocation ->
							context.report(
								ISSUE,
								field,
								typeLocation,
								"MutableLiveData should be replaced by KotlinMutableLiveData",
								lintFix
							)
						}

						if (!field.uastInitializer.toString().contains("null")) {
							field.uastInitializer?.let {
								context.getLocation(it)
							}?.let { initLocation ->
								context.report(
									ISSUE,
									field,
									initLocation,
									"MutableLiveData should be replaced by KotlinMutableLiveData",
									lintFix
								)
							}
						}
					}
			}
		}

	companion object {
		private val IMPLEMENTATION = Implementation(
			MutableLiveDataDetector::class.java,
			Scope.JAVA_FILE_SCOPE
		)

		val ISSUE = Issue.create(
			id = "MutableLiveDataUsed",
			briefDescription = "MutableLiveData is being used instead of KotlinMutableLiveData",
			explanation = "KotlinMutableLiveData should be used instead of MutableLiveData for null safety",
			category = Category.CORRECTNESS,
			severity = Severity.ERROR,
			implementation = IMPLEMENTATION
		)
	}
}
