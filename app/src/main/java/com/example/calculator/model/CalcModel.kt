package com.example.calculator.model

enum class OperatorTypes(val value: String) {
    plus("+"), minus("-"), multiply("*"), divide("/")
}

abstract class CalcNode<out T> (val value: T){
    override fun toString(): String {
        return value.toString()
    }

    override fun equals(other: Any?): Boolean {
        return if(other is CalcNode<*>) other.value == value else false
    }

    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }

}

class NumberNode(value: Number) : CalcNode<Number>(value){
    companion object {
        val zero: NumberNode
        get() = NumberNode(0)
    }
}

class OperatorNode(value: String): CalcNode<String>(value) {
    private val availableOperations: List<String> = OperatorTypes.values().map { it.value }
    fun calculate(a: Double, b: Double): Double{
        return when(value){
            !in availableOperations -> a
            OperatorTypes.plus.value -> a + b
            OperatorTypes.minus.value -> a - b
            OperatorTypes.multiply.value -> a * b
            OperatorTypes.divide.value -> a / b
            else -> a
        }
    }
}
