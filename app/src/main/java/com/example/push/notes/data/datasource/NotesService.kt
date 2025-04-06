package com.example.push.notes.data.datasource

import com.example.push.notes.data.model.NoteRequest
import com.example.push.notes.data.model.NoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NotesService {
    @GET("notes/today")
    suspend fun getNotes(): Response<List<NoteResponse>>

    @POST("notes")
    suspend fun newNote(@Body request: NoteRequest): Response<NoteResponse>
}