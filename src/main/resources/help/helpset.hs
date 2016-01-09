<?xml version='1.0' encoding='ISO-8859-1'?>
<!DOCTYPE helpset PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 2.0//EN"
                         "http://java.sun.com/products/javahelp/helpset_2_0.dtd">

<helpset version="2.0" xml:lang="en">


	<title>AstroJournal</title>
	<maps>
		<homeID>Index</homeID>
		<mapref location="map.xml"/>
	</maps>
	
	<view xml:lang="en" mergetype="javax.help.AppendMerge">
		<name>TOC</name>
		<label>Table of Contents</label>
		<type>javax.help.TOCView</type>
		<data>toc.xml</data>
	</view>
	
	<view xml:lang="en" mergetype="javax.help.AppendMerge">
		<name>Index</name>
		<label>Index</label>
		<type>javax.help.IndexView</type>
		<data>Index.xml</data>
	</view>
	
	<!--
	The command jhindexer is required for generating the folder JavaHelpSearch containing 
	the index to be used for JHelp Search. Unfortunately, this program is no longer available.
	I currently disable the Search menu.
	<view xml:lang="en" >
		<name>Search</name>
		<label>Search</label>
		<type>javax.help.SearchView</type>
		<data engine="com.sun.java.help.search.DefaultSearchEngine">JavaHelpSearch</data>
	</view>
	-->	
    
</helpset>
