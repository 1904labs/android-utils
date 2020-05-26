package com.labs1904.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue
import com.labs1904.lint.detectors.CompositeDisposableClearDetector
import com.labs1904.lint.detectors.DisposableClearDetector

@Suppress("UnstableApiUsage", "Unused")
class LintIssueRegistry : IssueRegistry() {
	override val issues: List<Issue>
		get() = listOf(
			CompositeDisposableClearDetector.ISSUE,
			DisposableClearDetector.ISSUE
		)
}
