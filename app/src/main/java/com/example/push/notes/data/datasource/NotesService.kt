package com.example.push.notes.data.datasource

import com.example.push.notes.data.model.NewNoteRequest
import com.example.push.notes.data.model.NewNoteResponse
import com.example.push.notes.data.model.NoteListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface NotesService {
    @GET("notes/today")
    suspend fun getNotes(): Response<NoteListResponse>



    @POST("notes")
    suspend fun newNote(
        @Body request: NewNoteRequest
    ): Response<NewNoteResponse>

}