package com.example.push.notes.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.push.notes.data.model.NoteResponse


@Composable
fun NotesUI(
    noteViewModel: NoteViewModel,
    navigateToNewNote: () -> Unit,

) {
    val notes by noteViewModel.notes.observeAsState(emptyList())

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(235, 235, 235))
            .padding(vertical = 50.dp, horizontal = 10.dp)
    ) {
        Text(
            text = "Your notes",
            fontSize = 40.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navigateToNewNote() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFC6F177),
            )
        ) {
            Text(
                text = "Add a note",
                color = Color(0xff002c2b),
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn {
          //aqui van las notas
        }

    }


}

@Composable
fun Note(note: NoteResponse) {
    Column(
        modifier = Modifier
            .background(Color(0xFF8BC34A))
            .padding(vertical = 30.dp, horizontal = 10.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = note.content, fontSize = 25.sp, color = Color(0xFF0F5B12))

    }
    Spacer(modifier = Modifier.height(30.dp))

}