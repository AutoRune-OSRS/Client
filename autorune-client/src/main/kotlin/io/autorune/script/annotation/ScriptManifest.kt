package io.autorune.script.annotation

import io.autorune.script.category.ScriptCategory

@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class ScriptManifest(val name: String, val author: String, val version: Double, val description: String,
                                val category: ScriptCategory = ScriptCategory.UNDEFINED)