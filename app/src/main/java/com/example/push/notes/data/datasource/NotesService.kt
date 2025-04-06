package com.example.push.notes.data.datasource

import com.example.push.notes.data.model.NewNoteRequest
import com.example.push.notes.data.model.NewNoteResponse
import com.example.push.notes.data.model.NoteRequest
import com.example.push.notes.data.model.NoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface NotesService {
    @GET("notes/today")
    suspend fun getNotes(
        @Header("Authorization") token: String
    ): Response<List<NoteResponse>>


    @POST("notes")
    suspend fun newNote(
        @Header("Authorization") token: String,
        @Body request: NewNoteRequest
    ): Response<NewNoteResponse>

}