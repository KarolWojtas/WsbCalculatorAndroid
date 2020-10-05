package com.example.calculator.model

import android.view.View

enum class CalcNodeTypes {
    operator, digit
}

abstract class CalcNode<out T> (val type: CalcNodeTypes, val value: T){
    override fun toString(): String {
        return value.toString()
    }
}

class NumberNode(value: Number) : CalcNode<Number>(CalcNodeTypes.digit, value){
    companion object {
        val zero: NumberNode
        get() = NumberNode(0)
    }
}

class OperatorNode(value: String): CalcNode<String>(CalcNodeTypes.operator, value)
