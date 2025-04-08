package com.example.push.notes.data.datasource

import com.example.push.notes.data.model.NewNoteRequest
import com.example.push.notes.data.model.NewNoteResponse
import com.example.push.notes.data.model.NoteListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface NotesService {
    @GET("notes/today")
    suspend fun getNotes(): Response<NoteListResponse>



    @Multipart
    @POST("notes")
    suspend fun newNote(
        @Part("content") content: RequestBody,
        @Part("emotionId") emotionId: RequestBody,
        @Part("type") type: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Response<NewNoteResponse>

}