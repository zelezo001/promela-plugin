# Promela plugin for IntelliJ IDEA

The plugin adds support for the Promela language and the Spin model checker to the IntelliJ IDEA IDE,
which I built as a part of my bachelor's thesis on MFF UK. 

## Features

- Coding assistance (highlighting, context-aware completion, formatting, etc.)
- Navigation and search (jump to declarations, find usages, structure view, documentation)
- Verification and simulation (integrated Spin execution via Run Configurations)
- State automata rendering for opened models
- Partial support for preprocessor directives in Promela source doe (#include not included)

## Getting Started

Install a compatible JetBrains IDE, such as IntelliJ IDEA (2026.1+)
Ensure the Spin binary and a C compiler are installed on your system
Launch the IDE, search for "Promela" in plugin settings, and click install. 
You can also build the plugin yourself (see Build).

## Spin integration

To make use of Spin-related features (automata, simulation/verification runs). Modules containing Promela files must have sdk set to "Spin SDK" and must be located inside a source content root.

## Build

To build this plugin JDK 21 is expected to be installed. 
Plugin can be build with Gradle by cloning this repository and calling
```
./gradlew buildPlugin
```
You can then use the built zip file in `build/distributions/` to install the plugin manually.

## Contributing to this project
If you want to contribute to this project, just fork this repository, do your thing and open a pull request.  
