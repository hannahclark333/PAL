package com.csci448.hannah.pal.ui.screens

import com.csci448.hannah.pal.viewmodel.PokemonTeamViewModel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable


@Composable
fun OptimizationDropdown(
    viewModel: PokemonTeamViewModel,
    onNext: (String) -> Unit = {}
) {
    val expanded = viewModel.expanded.value
    val selectedIndex = viewModel.selectedIndex.value
    val items = viewModel.options

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Team Optimized for:",
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openDropdown() }
                    .background(Color.White)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = items[selectedIndex],
                    fontSize = 24.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = if (expanded) "▲" else "▼",
                    fontSize = 28.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }

            // Dropdown menu
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { viewModel.closeDropdown() },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                items.forEachIndexed { index, label ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = label,
                                fontSize = 22.sp,
                                color = Color.Black
                            )
                        },
                        onClick = { viewModel.selectOption(index) },
                        contentPadding = PaddingValues(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { onNext(viewModel.selectedLabel) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            Text("Next", fontSize = 22.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OptimizationDropdownPreview() {
    val vm = PokemonTeamViewModel()
    OptimizationDropdown(viewModel = vm)
}

