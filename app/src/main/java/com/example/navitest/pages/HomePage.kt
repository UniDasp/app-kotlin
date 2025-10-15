package com.example.navitest.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomePage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize()
            .background(Color.Blue),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "home",
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 40.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray
        )

        Card(
            modifier = Modifier
                .padding(
                    top = 20.dp,
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 20.dp
                )
                .background(Color.White)
                .weight(1f),
        ) {
            Text("Card in Home",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )

            var countClicks by remember { mutableIntStateOf(0) }

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(20.dp),
                onClick = {
                    countClicks++
                }
            ) {
                Text("Clicks $countClicks")
            }
        }
    }
}