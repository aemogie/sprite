@file:JvmName("Console")

package io.github.aemogie.timble.utils.console

import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.FileOutputStream

val STD_OUT: FileOutputStream = FileOutputStream(FileDescriptor.out)
val STD_ERR: FileOutputStream = FileOutputStream(FileDescriptor.err)
val STD_IN: FileInputStream = FileInputStream(FileDescriptor.`in`)