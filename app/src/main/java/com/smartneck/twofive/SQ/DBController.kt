package com.smartneck.twofive.SQ

import android.content.Context

class DatabaseController
{
    private constructor(context: Context)
    {
        // ...
    }

    companion object
    {
        private var instance: DatabaseController? = null
        fun getInstance(context: Context): DatabaseController
        {
            if(instance == null)
            {
                instance = DatabaseController(context)
            }

            return instance!!
        }
    }
}