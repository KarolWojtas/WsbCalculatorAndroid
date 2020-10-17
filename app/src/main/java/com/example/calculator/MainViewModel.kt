package com.example.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.calculator.model.CalcNode
import com.example.calculator.model.NumberNode
import com.example.calculator.model.OperatorNode

const val defaultEnteredValue = "0"

class MainViewModel: ViewModel() {

    private val _nodes: MutableLiveData<MutableList<CalcNode<Any>>> = MutableLiveData(mutableListOf())

    val nodes: LiveData<MutableList<CalcNode<Any>>>
    get() = _nodes

    private fun addNode(node: CalcNode<Any>){
        _nodes.value = _nodes.value?.apply { add(node) }
    }

    private val _enteredNumber = MutableLiveData(defaultEnteredValue)

    val enteredNumber: LiveData<String>
    get() = _enteredNumber

    val canEnterDigit: LiveData<Boolean> = _nodes.map {
        val lastNode = it.lastOrNull()
        lastNode is OperatorNode || it.isEmpty()
    }

    private fun addNumber(value: Number, isCalculated: Boolean = false){
        addNode(NumberNode(value, isCalculated))
    }

    /**
     * adding operator first appends entered value to node list if defined
     * secondly if operator was already added as last node, we replace it (only one operator between numbers)
     */
    fun addOperator(value: String){
        val enterNumberNumerical = _enteredNumber.value?.toIntOrNull()
        if(enterNumberNumerical != null && enterNumberNumerical != 0){
            addNumber(enterNumberNumerical)
            _enteredNumber.value = defaultEnteredValue
        }
        val lastNode = _nodes.value?.lastOrNull()
        if(lastNode is OperatorNode){
            _nodes.value?.removeLast()
        }

        addNode(OperatorNode(value))
    }

    fun appendDigit(value: Number){
        _enteredNumber.value = if(_enteredNumber.value == defaultEnteredValue) value.toString() else _enteredNumber.value+value
    }

    val nodesLiteral = nodes.map {
        it.joinToString(" ")
    }

    fun reset(){
        _enteredNumber.value = defaultEnteredValue
        _nodes.value = mutableListOf()
    }

    /**
     * present
     */
    fun equals(){
        _nodes.value?.let { nodes ->
            val lastNode = nodes.last()
            // break if last node is a result, use has to enter something after calculating
            if(lastNode is NumberNode && lastNode.isCalculated)
                return
            _enteredNumber.value?.toInt()?.let { addNumber(it) }
            // find last "=" index
            val lastEqualsIndex = nodes.indexOfLast { it.value == "=" }
            val lastOperationChain = nodes.filterIndexed { index, _ ->  index > lastEqualsIndex}
            addNode(OperatorNode("="))
            var currentOperator: OperatorNode? = null
            var accumulator = 0.0
            for ( nodeIx in lastOperationChain.indices){
                val currentNode = lastOperationChain[nodeIx]
                if(currentNode is OperatorNode){
                    currentOperator = currentNode
                } else if (currentNode is NumberNode){
                    accumulator = currentOperator?.calculate(accumulator, currentNode.value.toDouble())?:currentNode.value.toDouble()
                }
            }
            addNumber(if(accumulator % 1 != 0.0) accumulator else accumulator.toInt(), true)
            _enteredNumber.value = defaultEnteredValue
        }

    }
}