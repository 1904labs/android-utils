package com.labs1904.lint.detectors

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

@Suppress("UnstableApiUsage")
class NavControllerDetector : Detector(), SourceCodeScanner {

	override fun getApplicableMethodNames() = listOf("navigate")

	override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
		if (context.evaluator.isMemberInClass(method, "androidx.navigation.NavController")) {
			val lintFix = LintFix.create()
				.name("Replace with navigateSafe")
				.replace()
				.text(node.methodName)
				.with("navigateSafe")
				.autoFix()
				.build()

			context.report(
				ISSUE,
				node,
				context.getNameLocation(node),
				"Using navigate() instead of navigateSafe()",
				lintFix
			)
		}
	}

	companion object {
		private val IMPLEMENTATION = Implementation(
			NavControllerDetector::class.java,
			Scope.JAVA_FILE_SCOPE
		)

		val ISSUE = Issue.create(
			id = "NavigateSafe",
			briefDescription = "NavController.navigateSafe() should be used instead of NavController.navigate()",
			explanation = "A bug exists within NavController.navigate() that can sometimes cause " +
				"the application to crash. NavController.navigateSafe() should be used instead.",
			category = Category.CORRECTNESS,
			severity = Severity.ERROR,
			implementation = IMPLEMENTATION
		)
	}
}
