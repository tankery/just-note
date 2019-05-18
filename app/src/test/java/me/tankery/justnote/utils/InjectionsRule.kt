package me.tankery.justnote.utils

import androidx.test.core.app.ApplicationProvider
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class InjectionsRule : TestRule {
    override fun apply(base: Statement?, description: Description?) = object : Statement() {
        override fun evaluate() {
            Injections.application = ApplicationProvider.getApplicationContext()
            base?.evaluate()
        }
    }
}