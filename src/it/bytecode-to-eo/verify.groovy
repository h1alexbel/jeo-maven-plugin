/*
 * SPDX-FileCopyrightText: Copyright (c) 2016-2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
//Check logs first.
String log = new File(basedir, 'build.log').text;
assert log.contains(" Disassembling files from ")
assert log.contains("Application.class disassembled to")
assert log.contains("Foo.class disassembled to ")
assert log.contains("WithoutPackage.class disassembled to ")
assert log.contains("BUILD SUCCESS")
//Check that we have generated XMIR object file.
assert new File(basedir, 'target/generated-sources/jeo-phi/org/eolang/jeo/Application.phi').exists()
File app = new File(basedir, 'target/generated-sources/jeo-xmir/org/eolang/jeo/Application.xmir')
assert app.exists()
assert app.text.contains("<listing>") : "We enabled listings (see the omitListings=false option) in the XMIR file, but it is absent"
app.eachLine { line ->
  if(line.contains("meta")) {
    def spaces = line.takeWhile { it == ' ' }.size()
    assert spaces % 3 == 0 : "Line has incorrect indentation (${spaces} spaces): $line"
  }
}
true
