asm2dot
=======

A quick and dirty ARM ASM control flow grapher.

To build, run `mvn`. A complete jar is dropped into target/.

Antlr 4 grammar is in src/main/antlr4
Other instructions will need defining in there.

`xasm` requires `xdot`, takes a .s file, presents the cfg on screen.
