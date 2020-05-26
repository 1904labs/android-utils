package com.labs1904.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue

@Suppress("UnstableApiUsage", "Unused")
class LintIssueRegistry : IssueRegistry() {
	override val issues: List<Issue>
		get() = listOf(

		)
}
