# MemeBot
## Introduction ##
MemeBot is a simple discord bot written in Java using JDA.

## How To Compile ##
Make sure you have maven installed, then run:
`mvn clean compile assembly:single`
which will produce a JAR in the /target directory called `MemeBot-VERSION-jar-with-dependencies.jar`

## How To Run ##
Run the following command:
`java -jar MemeBot-VERSION-jar-with-dependencies.jar SECRET`
replacing SECRET with the client secret provided by Discord.
