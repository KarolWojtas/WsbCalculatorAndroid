package com.example.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.calculator.model.CalcNode
import com.example.calculator.model.NumberNode
import com.example.calculator.model.OperatorNode

class MainViewModel: ViewModel() {
    val mock = "halko"
    private val _nodes: MutableLiveData<MutableList<CalcNode<Any>>> = MutableLiveData(mutableListOf(NumberNode.zero))

    val nodes: LiveData<MutableList<CalcNode<Any>>>
    get() = _nodes

    private fun addNode(node: CalcNode<Any>){
        _nodes.value = _nodes.value?.apply { add(node) }
    }

    fun addNumber(value: Number){
        addNode(NumberNode(value))
    }

    fun addOperator(value: String){
        addNode(OperatorNode(value))
    }

    val nodesLiteral = nodes.map {
        it.joinToString(" ")
    }
}