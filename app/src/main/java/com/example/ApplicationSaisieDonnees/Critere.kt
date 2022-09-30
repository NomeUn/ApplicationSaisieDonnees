 package com.example.ApplicationSaisieDonnees

import java.io.Serializable

data class Critere(
        public val nomCritere:String,
        public val nbArrondi: String,
        public val min: String,
        public val max: String,
        public val serie: String
) : Serializable