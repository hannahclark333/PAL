package com.csci448.hannah.pal.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class PokemonTeamViewModel : ViewModel() {

    // Whether the dropdown is open
    private val _expanded = mutableStateOf(false)
    val expanded: State<Boolean>
        get() = _expanded

    // List of optimization options
    private val _options = listOf(
        "Speed",
        "Defense",
        "Special Attack",
        "Balanced",
    )
    val options: List<String>
        get() = _options

    private val _selectedIndex = mutableStateOf(0)
    val selectedIndex: State<Int>
        get() = _selectedIndex
    val selectedLabel: String
        get() = _options[_selectedIndex.value]

    // --- Mutators ---

    fun openDropdown() {
        _expanded.value = true
    }

    fun closeDropdown() {
        _expanded.value = false
    }

    fun selectOption(index: Int) {
        _selectedIndex.value = index
        _expanded.value = false
    }
}
