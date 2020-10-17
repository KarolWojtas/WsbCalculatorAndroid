package com.example.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.calculator.model.CalcNode
import com.example.calculator.model.NumberNode
import com.example.calculator.model.OperatorNode

class MainViewModel: ViewModel() {
    private val _nodes: MutableLiveData<MutableList<CalcNode<Any>>> = MutableLiveData(mutableListOf())

    val nodes: LiveData<MutableList<CalcNode<Any>>>
    get() = _nodes

    private fun addNode(node: CalcNode<Any>){
        _nodes.value = _nodes.value?.apply { add(node) }
    }

    private val _enteredNumber: MutableLiveData<String> = MutableLiveData("0")

    val enteredNumber: LiveData<String>
    get() = _enteredNumber

    fun addNumber(value: Number){
        if(_nodes.value?.size == 1)
        addNode(NumberNode(value))
    }

    fun addOperator(value: String){
        addNode(OperatorNode(value))
    }

    fun appendDigit(value: Number){
        _enteredNumber.value = if(_enteredNumber.value == "0") value.toString() else _enteredNumber.value+value
    }

    val nodesLiteral = nodes.map {
        it.joinToString(" ")
    }
}