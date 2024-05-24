# Play Utilities
(This project is distributed under the MIT License - see [LICENSE](LICENSE) and [AUTHORS](AUTHORS))

Some utilities for use with the [Play framework](https://www.playframework.com/).
Split off from earlier projects to be used in [Rasbeb 2.0](https://github.com/kcoolsae/Rasbeb2) but can
be useful in other web application based on Play.

1. **Deputies** Controllers delegate to deputies to ensure thread safety
2. Templates gain access to configuration and internationalization info
3. Support for tables in templates

## Building this library
Instructions below are for the Linux operating system. The library can possibly also be built
on MacOS and/or Windows (bash), but we do not provide instructions for this.

You need
* Java version 17 or newer
* SBT build system, version 1.9.3 or newer

The following command, executed from the top level project directory, installs the library into the local ivy repository 

    $ sbt clean publishLocal

making it available to other (local) Play projects.
