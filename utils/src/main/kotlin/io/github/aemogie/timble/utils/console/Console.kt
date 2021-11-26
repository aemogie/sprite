@file:JvmName("Console")

package io.github.aemogie.timble.utils.console

import java.io.*

@JvmField val STD_OUT: FileOutputStream = FileOutputStream(FileDescriptor.out)
@JvmField val STD_ERR: FileOutputStream = FileOutputStream(FileDescriptor.err)
@JvmField val STD_IN: FileInputStream = FileInputStream(FileDescriptor.`in`)