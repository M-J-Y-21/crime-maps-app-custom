package com.example.map1st

import java.lang.IllegalArgumentException

fun main() {
    // var is mutable , val is not
    // Use TODO to save something TODO at Bottom
    // multi line comment is /*<multiLineComment*/

    // Integer Types: Byte (8 bit), Short (16 bit),
    // Int (32 bit), Long (64 bit)
    // If don't explicitely give type to number kotlin will usually infer an Int

    val myByte: Byte = 13
    val myShort: Short = 125
    val myInt: Int = 123123123
    val myLong: Long = 324325454645756534

    // Floating point number types: Float (32 bit), Double (64 bit)
    // to explicitely state a Float add f at end
    val myFloat = 13.23F
    val myDouble = 3.2434345456456243

    // Boolean data type
    // don't need to explicitely state boolean
    var isSunny: Boolean = true
    isSunny = false

    // char is assigned with single quotes
    var letterChar = 'A'
    var digitChar = '1'

    var myName = "Matan"
    // to get last char
    var lastChar = myName[myName.length - 1]
    // or can just plug expression straight in
    // println("Hello $myName Last Char is - ${myName[myName.length - 1]}")
    var myNum = 0
    // ${myNum++} will increment myNum for the next time
    // println("myNum incremented is ${++myNum}")

    // Arithmetic operators (+, -, *, /, %)
    var moduloResult = 15 % 2
    // module says for a % b, after subtracting b from a as many times as you can before turning a neg, what remains of a
    // println(moduloResult)

    // isEqual here will be false
    val isEqual = 5 == 3
    // can put a comparison in print like below
    // println("is5greter3 ${5>3}")

    // Assignment operators (+=, -=, *=, /=, %=)
    // Can increment with ++ and decrement with --

    var myHeight = 239
    var otherHeight = 220

//    if (myHeight > otherHeight) {
//        println("I'm taller")
//    } else if (myHeight == otherHeight) {
//        println("We're the same height")
//    } else {
//        println("I'm shorter")
//    }

//    var isRainy = true
//    if (isRainy) {
//        println("It's Rainy son!")
//    }

    // When expressions in Kotlin are like Switch statements in Java

    var season = 3
    // doesn't have to be a integer
//    when (season) {
//        1 -> println("Spring")
//        2 -> println("Summer")
//        3 -> {
//            println("Fall")
//            println("Autumn")
//        }
//        4 -> println("Winter")
//        // else if nothing else matches
//        else -> println("Invalid Season")
//    }

    var month = 3
    // in 3..5 for all nums 3 to 5 inclusive
//    when (month) {
//        in 3..5 -> println("Spring")
//        in 6..8 -> println("Summer")
//        in 9..11 -> println("Fall")
//        12, 1, 2 -> println("Winter")
//        else -> println("invalid season")
//    }

    var age = 23
    // !in 0..20 -> println("Drink") , says if not in 0-20 i.e. 21 or older
//    when (age) {
//        !in 0..20 -> println("Drink")
//        in 18..20 -> println("Don't Drink")
//        else -> println("Wait")
//    }

    var x: Any = "13.37F"
//    when (x) {
//        is Int -> println("$x is an Int")
//        !is Double -> println("$x is not an Double")
//        is String -> println("$x is a string")
//        is Float -> println("$x is a float")
//        else -> println("$x is none of the above")
//    }

    // while loops are quite similar to java and C
    var y = 100
//    while (y >= 0) {
//        print("$y")
//        y-= 2
//    }
//    println("\nWhile loop is done")

    // can also use do while loops in kotlin which remember run at least once
    var j = 1
//    do {
//        j++
//    } while (j <= 10)
//
//    println("$j Hy")

    // for loop is mainly used to iterate over objects
    // while loops for executing code as long as conditions are met

    var feltTempt = "cold"
    var roomTemp = 10
//    while (feltTempt == "cold") {
//        roomTemp++
//        if (roomTemp >= 20) {
//            feltTempt = "comfy"
//            println("IT's $feltTempt now")
//        }
//    }

    // this notation is 1 to 10 inclusive
//    for (num in 1..10) {
//        print(num)
//    }
//
//    // this notation is 1 to 10 inclusive 1 but not 10
//    for (i in 1 until 10) {
//        print(i)
//    }
//
//    println()
//    // downto keyword goes from 10 downto 1 inclusive of both, can use keyword step
//    // same as - for (i in 10.downTo(1).step(2))
//    for (j in 10 downTo 1 step 2) {
//        print("$j ")
//    }

    // when passing values 5 and 10 they're called arguements
//    println(addUp(5, 10))
//    println(sumAverage(10.0, 5.0))

    // var name : String = "Matan"
    // name = null -> Compilation Error
    // the ? after the type allows the variable to be assigned to null
    var nullableName : String? = "Matan"
    // nullableName = null

    // var len = name.length
    // var len2 = nullableName.length gives error as null

    // old check
//    if (nullableName != null) {
//        var len2 = nullableName.length
//    } else {
//        null
//    }
    // new check for kotlin
//    var len2 = nullableName?.length
//    println(nullableName?.let { it.toLowerCase() })
//
//    // below says only if value is not null execute code
//    nullableName?.let { println(it.length) }
//
//    // says below if null get default value of Guest, ?: Elvis operator
//    val name = nullableName ?: "Guest"
//    println(name)
//
//    // if not null take to lower case if null though will throw null pointer exception
//    println(nullableName!!.toLowerCase())


//    var jim = Person("Jim", "Halpert")
//    jim.hobby = "Guitar"
//    jim.stateHobby()

//    var myCar = Car()
//    println("${myCar.myBrand}")
//    println("${myCar.maxSpeed}")

    var myAnimal = Animal("Bob", 10)
    var myDog = Dog("Blue", 10, "Strong")
    myDog.extentLife(10.0)

    myAnimal.run(200.0)
    myDog.run(200.0)



}

class Car () {
    lateinit var owner : String

    val myBrand: String = "BMW"
        // Custom getter
        get() {
            return field.toLowerCase()
        }

    var maxSpeed : Int = 250
    set(value) {
        field = if(value > 0) value else throw IllegalArgumentException("Max Speed cannot be less then zero")
    }

    // private means can only set in the same class, in for eg the init function
    var myModel : String = "M5"
        private set

    init {
        this.owner = "Jim"
    }
}

// need to be open class to use inheritance
// Animal is superclass, parentclass or baseclass
open class Animal(val name: String, val age: Int) {
    var lifeExpectancy: Double = 0.0

    fun extentLife(amount: Double) {
        if (amount > 0)
            lifeExpectancy += amount
    }

    open fun run(distance: Double) {
        println("Ran for $distance KM")
    }

}
// Dog is subclass of animal also called child class or derived class of animal
// sealed means class can't be inherited from
// Need to input inherited parameters
class Dog(name: String, age: Int, barkStrength: String) : Animal(name, age) {

    override fun run(distance: Double) {
        println("Ran like a dog for $distance KM")
    }

}

// init method will do something on creation of object
// in the () is the constructor
// added default values below
class Person(firstName: String = "John", lastName: String = "Doe") {
    // Member Variables - properties are the same
    // program will run if we have the age or not
    var age : Int? = null

    var hobby : String = "watch Netflix"

    // Member functions - also called methods they're functions within a class
    fun stateHobby() {
        println("My hobby is $hobby")
    }
}


// Method - a method is a function within a class

// takes into Ints x and y and returns an Int
// the return is specified with the : after ()
// x and y below are parameters
fun addUp(x: Int, y: Int) : Int {
    return x + y
}

fun sumAverage(x: Double, y: Double) : Double {
    return (x + y)/ 2
}


