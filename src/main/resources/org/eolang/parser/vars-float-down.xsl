<?xml version="1.0" encoding="UTF-8"?>
<!--
The MIT License (MIT)

Copyright (c) 2016-2024 Objectionary.com

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="vars-float-down" version="2.0">
  <!--
  The transformation does the opposite of "/org/eolang/parser/vars-float-up.xsl".

  At the level of EO it looks like:

  [] > object             [] > object
    opcode > somebody       seq > @
      42               =>     opcode > somebody
    seq > @                     42
      somebody

  In XMIR it works via "ref" and "line" attributes, it the transformation must
  be applied after "/org/eolang/parser/add-refs.xsl"

  <o>                                         <o>
    <o ref="22" base="abc" line="3"/>           <o line="22" base="xyz" name="hello"/>
  </o>                                   =>   </o>
  ...
  <o line="22" base="xyz" name="hello"/>

  -->
  <xsl:output encoding="UTF-8" method="xml"/>
  <xsl:template match="//o[@ref and @ref!='']">
    <xsl:variable name="reference" select="@ref"/>
    <xsl:variable name="found" select="//o[@line=$reference]"/>
    <xsl:copy-of select="$found"/>
  </xsl:template>
  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="o[@line=//o[@ref]/@ref and not(preceding-sibling::o[@ref=@line])]"/>
</xsl:stylesheet>