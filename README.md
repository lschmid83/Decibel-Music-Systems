# Decibel Music Systems

These projects are a collection of frontend client sample applications which retrieve music metadata from the Decibel Music Systems API which I created. Unfortunately the API is now deprecated and the company has closed for business so they are non-functional and do not return any data. It would be possible to modify the source code to request music metadata from alternative APIs such as MusicBrainz or Discogs if anyone wanted to get them working again.

I have also included the API documentation HTML website which includes instructions on how to use the client applications.

Here are some screenshots of the project:

<img align='left' src='https://drive.google.com/uc?id=1MulUZxoZ672gE4aklhc42yES3Bz3cq27' width='240'>
<img align='left' src='https://drive.google.com/uc?id=1SVjUQdZR4Kam5072yGf3CbBVRtwu-RhK' width='240'>
<img src='https://drive.google.com/uc?id=1JwXKymPSDVFpsJllvJm7vRilVnOr3tjj' width='240'>

C# Client
=========

The C# Client is a VS2022 C# WPF application which can be opened from the CSharpClient.sln file.

Java Client
===========

The Java Client is a Java Eclipse Swing application it can be opened in Eclipse:

1. Select File -> Import...
2. General -> Existing Projects into Workspace
3. Browse to the root directory in the source code folder Client Applications\Java Client
4. Select Copy projects into Workspace and Finish

PHP Client
==========

The PHP client can be opened in a browser using WAMP server.

Download the required software here:

[WAMP Server 2.5](https://drive.google.com/file/d/1dZvYppg4sn7IBpMiJEWck6_hY2e_Txcc/view?usp=sharing)

1. Create a folder called decibel-php-client in the C:\wamp\www\ folder
2. Copy the contents of the source code folder Client Applications\PHP Client to the newly created folder
3. Open http://localhost:8080/decibel-php-client/ in a browser

API Documentation Website
=========================

The source code folder API Documentation contains the documentation for the Decibel API. Open the index.html file and you will be able to browse the API methods and documentation and view code examples from the sample client application projects.






















