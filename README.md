QA_sorting is an open source software Q&A document recommandation tool and framework based on code pattern. It is task oriented, aiming to solve programming task problems in practice. QA_sorting can be used as a software Q&A document recommandation tool, a library, or a framework.

# Software License

This software licensed under MIT Open Source.

The License text for this software can be found in [LICENSE](LICENSE) in the root folder of the project.

# Requirements

Java Runtime Environment 1.8

Windows, Linux or Mac

# Installation

#### Run from source code

* Clone this repository to local.
* Execute command line `./gradlew run` for Linux and Mac, or `./gradlew.bat run` for Windows in the root directory.

#### Run from binary distribution

* Download the binary distribution [qa_sorting.jar](https://woooking.github.io/qa_sorting/build/libs/qa_sorting.jar).
* Execute command line `java -jar qa_sorting.jar` in the directory where you put the jar at.

# Project Structure

    ./build
    ./lib
    ./src/analyze
    ./src/main
    ./gradle
    LICENSE
    README.md
    build.gradle
    gradlew
    gradlew.bat
    settings.gradle

The ``./build`` folder contains the document and binary distribution.

The ``./lib`` folder contains the libraries not included in maven.

The ``./src/analyze`` folder contains the source code to analyze the effect of the algorithm.

The ``./src/main`` folder contains the main source code of this project.

The ``LICENSE`` file contains the license text of this project.

The ``README.md`` file is the file you are reading now.

The ``./gradle``, ``build.gradle``, ``gradlew``, ``gradlew.bat`` and ``settings.gradle`` are directory and files for gradle build tool.

# Getting Started

### QA_sorting as a tool

Run the server as the Installation part, visit "localhost:8080" in your web broswer. Input your query in the inputbox and press enter. In the result screen, the left part is the origin result and the right part is the new result using our algorithm.

### QA_sorting as a library

Add the jar to your project. Customize your own documents by implementing interface ``com.github.woooking.qa_sorting.document.IDocument``. Then using sort api ``com.github.woooking.qa_sorting.api.QASorting.sort(java.util.List<T> documents)`` to sort documents.

If you want to further customize the sort algorithm, take a look at [javadoc](https://woooking.github.io/qa_sorting/build/docs/javadoc/index.html).

### QA_sorting as a framework

Add the jar to your project. Customize your own documents as described above. Besides, you should also implement ``com.github.woooking.qa_sorting.document.IIndexer`` to tell the server how to get documents from a query. Create a ``com.github.woooking.qa_sorting.api.QAServer``, add services by ``addService``, finally call ``luanch`` to run the server.

``com.github.woooking.qa_sorting.document.stackoverflow.SOServer`` could be an example to start with.


# Document
The javadoc of this project can be found [here](https://woooking.github.io/qa_sorting/build/docs/javadoc/index.html)


