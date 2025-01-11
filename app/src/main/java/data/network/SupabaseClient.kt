package data.network

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue

object SupabaseClient {
    val client = createSupabaseClient{
        supabaseUrl = https://cdnhesvkytedtxvimved.supabase.co
        supabasekey = eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNkbmhlc3ZreXRlZHR4dmltdmVkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzY2MTk0NTIsImV4cCI6MjA1MjE5NTQ1Mn0.OSG-5szrCP8tYuMZN97qOjPlGlMpRSik-5Nf860dySo
        ) { SupabaseClientBuilder
            install(GoTrue)
        }
    }
}