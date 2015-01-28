eXparity Bean  [![Build Status](https://travis-ci.org/eXparity/exparity-bean.svg?branch=master)](https://travis-ci.org/eXparity/exparity-bean) [![Coverage Status](https://coveralls.io/repos/eXparity/exparity-bean/badge.png?branch=master)](https://coveralls.io/r/eXparity/exparity-bean?branch=master)
=============

A Java library of bean utilities for manipulating and inspecting Java classes implementing the Java Beans standard.

Licensed under [BSD License][].

Downloads
---------
You can obtain the eXparity-bean jar from [maven central][]. To include your project in:

A maven project

    <dependency>
        <groupId>org.exparity</groupId>
        <artifactId>exparity-bean</artifactId>
        <version>1.0.3</version>
    </dependency>

A project which uses ivy for dependency management

    <dependency org="org.exparity" name="exparity-bean" rev="1.0.3"/>
            
Usage
-------------

The utilites are exposed from three class, for *Type*, *Bean*, and *Graph*. To clarify the difference between Bean and a Graph in the library is that Bean will limit it's scope to just the instance passed into into, Graph will descend into the children of the instance as well i.e. perform a deep inspection.

Property level operations are exposed by two classes, *BeanProperty* or *TypeProperty*, depending on if you're inspecting an Instance or a Class respectively.

Some example usages are below. 

To get a list of the get/set properties on a Class you can use any of the 2 approaches below.

    List<TypeProperty> properties = Type.type(MyClass.class).propertyList();
    List<TypeProperty> properties = new Type(MyClass.class).propertyList();

which after static importing will look like this

    for ( TypeProperty property : type(MyClass.class).propertyList() ) {
    	System.out.println("Property " + property.getName() + " is type " + property.getType());
    }

A similar operation can be performed on an instance. For example

    List<BeanProperty> properties = Bean.bean(new MyClass()).propertyList();
    List<BeanProperty> properties = new Bean(new MyClass()).propertyList();

which after static importing will look like this

    for ( BeanProperty property : bean(new MyClass()).propertyList() ) {
    	System.out.println("Property " + property.getName() + " has value " + property.getValue() );
    }

Lastly the same operations are available on an entire object graph. For example

    List<BeanProperty> properties = Graph.graph(new MyClass()).propertyList();
    List<BeanProperty> properties = new Graph(new MyClass()).propertyList();

which after static importing will look like this

    for ( BeanPropertyInstance property : graph(new MyClass()).propertyList() ) {
    	System.out.println("Property " + property.getName() + " has value " + property.getValue() );
    }

To get an individual property on a Class you can use the TypeProperty class directly. For example:

    TypeProperty property = Type.typeProperty(MyClass.class, "myProperty");

which after static importing can be used like this

    if ( typeProperty(MyClass.class, "myProperty").isList() ) {
    	// Do something
    }

To get an individual property on an instance you can use the BeanProperty class directly. For example:

    BeanProperty property = Bean.beanProperty(new MyClass(), "myProperty");

which after static importing can be used like this

    if ( beanProperty(new MyClass(), "myProperty").isList() ) {
    	// Do something
    }

The library includes methods for Class, instances, and object graphs to:

* __propertyList__ - Return a list of the Java Bean properties on a Class, instance, or object graph.
* __propertyMap__ - Return a map of the Java Bean properties on a Class, instance, or object graph keyed on the property name.
* __propertyType__ - Return the type for a property on a Class, instance, or object graph.
* __propertyNamed__ - Return a property instance for a property on a Class, instance, or object graph.
* __propertyValue__ - Return a value for a property on an instance or object graph.
* __hasProperty__ - Check if the property exists on a Class, instance, or object graph.
* __get__ - Return a property instance for a property on an instance, or object graph. (A synonym for propertyNamed).
* __setValue__ - Set the value a property on a an instance, or object graph.
* __find__ - Find all property instances on a Class, instance, or object graph which match a predicate.
* __findAny__ - Return the first property instance on a Class, instance, or object graph which matches a predicate.
* __visit__ - Visit all Java Bean properties on a Class, instance, or object graph.
* __apply__ - Apply a function to all properties on a Class, instance, or object graph which matches a predicate
* __simpleName__ - Return the class name for a Class or instance.
* __canonicalName__ - Return the full class name including package name for a Class or instance.
* __camelName__ - Return the class name for a Class or instance formatted using camel-case.
* __packageName__ - Return the package name for a Class or instance.
* __isArray__ - Return true if the Class or instance is an array.
* __isEnum__ - Return true if the Class or instance is an enumeration.
* __isPrimitive__ - Return true if the Class or instance is a primitive.
* __is__ - Test if the Class or instance is an instance of the given type.
* __superTypes__ - Return the supertypes of a Class or instance.
* __getType__ - Return the ypes of a Class or instance.

The library includes methods for properties to:

* __getName__ - Return the property name.
* __hasName__ - Test if the property has the given name.
* __getType__ - Return the type of the property.
* __getTypeCanonicalName__ - Return the full name including package of the Class of the property.
* __getTypeSimpleName__ - Return the name of the Class of the property.
* __getAccessor__ - Return the property accessor Method.
* __getMutator__ - Return the property mutator Method.
* __getDeclaringType__ - Return the Class the property is declared on.
* __getDeclaringTypeCanonicalName__ - Return the full name including packaging of Class the property is declared on.
* __getDeclaringTypeSimpleName__ - Return the name of the Class the property is declared on.
* __getTypeParameters__ - Return the generic parameters for the type of the property.
* __getTypeParameter__ - Return the nth generic parameter for the type of the property.
* __hasAnyTypeParameter__ - Test if any of the generic parameter types match any of the supplied types.
* __hasAnyTypeParameter__ - Test if any of the generic parameter types match the supplied type.
* __isType__ - Test if the property type is assignable from the supplied type.
* __isIterable__ - Test if the property type implements Iterable..
* __isGeneric__ - Test if the property type is generic.
* __isPrimitive__ - Test if the property type is primitive.
* __isArray__ - Test if the property type is an array.
* __isString__ - Test if the property type is a String.
* __isCharacter__ - Test if the property type is a Character or char.
* __isByte__ - Test if the property type is a Byte or byte.
* __isInteger__ - Test if the property type is an Integer or int.
* __isLong__ - Test if the property type is a Long or long.
* __isDouble__ - Test if the property type is a Double or double.
* __isFloat__ - Test if the property type is a Float or float.
* __isShort__ - Test if the property type is a Short or short.
* __isBoolean__ - Test if the property type is a Boolean or boolean.
* __isDate__ - Test if the property type is a java.util.Date.
* __isMap__ - Test if the property type implements Map.
* __isList__ - Test if the property type implements List.
* __isSet__ - Test if the property type implements Set.
* __isCollection__ - Test if the property type implements Collection.
* __isEnum__ - Test if the property type is a Java enum.

The Javadocs include examples on all methods so you can look there for examples for specific methods

Source
------
The source is structured along the lines of the maven standard folder structure for a jar project.

  * Core classes [src/main/java]
  * Unit tests [src/test/java]

The source includes a pom.xml for building with Maven 

Release Notes
-------------

Changes 1.0.2 -> 1.0.3
  * Drop dependency on exparity-stub

Changes 1.0.0 -> 1.0.1
  * Add dump methods to print out properties and their values
  * Add ordering of methods to make order consistent
  * Remove BeanUtils to simpligy API to use Bean.bean(..)
  * Remove GraphUtils to simplify API to use Graph.graph(..)

Acknowledgements
----------------
Developers:
  * Stewart Bissett


[BSD License]: http://opensource.org/licenses/BSD-3-Clause
[Maven central]: http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22exparity-bean%22
