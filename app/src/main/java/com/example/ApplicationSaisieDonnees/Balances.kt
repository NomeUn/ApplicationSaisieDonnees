package com.example.ApplicationSaisieDonnees

data class Balances (
    public val nom : String,
    public val vitesse : Int,
    public val parite : Int,
    public val nb_bits : Int,
    public val bits_stop : Int,
    public val porte : Int,
    public val isKilo : Int,
    public val asGChar : Int,
    public val chaineDemande : String,
    public val chaineDemandedsD : String,
    public val posistionDSD : Int,
    public val longueurDSD : Int,
    public val longueurStable : Int,
    public val ligneStable : Int,
    public val longueurInstable : Int,
    public val ligneInstable : Int
)