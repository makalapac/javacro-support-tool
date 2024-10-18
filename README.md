
# Support-tool SpringAI / Langchain4J demo

Spring boot Rest service application used for demo at Javacro2024 presentation "**_Unlocking AI Capabilities in Java: Exploring the Latest Tools_**". 
Presentation can be found at official [Javacro2024 agenda](https://2024.javacro.hr/Agenda#). 



## Installation

You need to obtain your OpenAI api key to use this demo.

  Before running this service, you need to run ChromaDB vector database docker image. You can run it with this command: 

  _docker run -it --rm --name chroma -p 8000:8000 ghcr.io/chroma-core/chroma:0.4.15_

  Inside service everything is preconfigured (default) for chromaDB. You can use another vector database if you want, but you need to swap out chromaDB dependency on pom.xml. 

  Hint: Run docker image before starting the application so auto initialization can initialize a collection. 

  Afterward, run the service, and it will also start the H2 database and initialize demo data (data.sql, schema.sql). 

  Then, use the embedding controller to start the embedding of terms from the resources folder.
  If you skip this process, a chat client will not have terms for correct cancellation policies. 

  Afterward, feel free to play with chat method to chat with support. 

  

     