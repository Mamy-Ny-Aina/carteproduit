package mg.itu.carteproduit

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
// Imports déjà prêts pour les TODO A et B — ne pas les supprimer :
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Mini-TP 4 — « Faire vivre un écran »
 *
 * Cette application affiche une carte produit STATIQUE.
 * Le log RECOMP (déjà en place) trace chaque (re)composition de la carte :
 * dans le Logcat d'Android Studio, filtrez sur :  tag:RECOMP
 *
 * Logs ajoutés pour le débriefing personnel (tag ACTION) :
 * ils tracent CHAQUE clic, qu'il déclenche ou non une recomposition —
 * à comparer avec RECOMP pour bien voir la différence entre
 * "un clic a eu lieu" et "une recomposition a eu lieu".
 * Filtrez avec :  tag:RECOMP|ACTION   (affiche les deux tags à la fois)
 *
 * Déroulé (voir l'énoncé) :
 *   1. LIRE ce fichier, puis remplir les deux prédictions du modèle
 *      — AVANT tout lancement.
 *   2. TODO A : le compteur de quantité (remember + mutableStateOf + Button). [FAIT]
 *   3. TODO B : la carte sélectionnable (état booléen + clickable + couleur). [FAIT]
 */

data class Produit(
    val nom: String,
    val origine: String,
    val prixKg: Double?,   // null = prix non encore fixé
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) {
                    ProduitCard(
                        Produit("Vanille Bourbon", "Sambava", 250_000.0)
                    )
                }
            }
        }
    }
}

@Composable
fun ProduitCard(produit: Produit) {
    // Ce log trace chaque (re)composition de la carte — NE PAS le déplacer.
    Log.i("RECOMP", "ProduitCard se (re)compose")

    // TODO A — FAIT : état du compteur, déclaré en haut de la fonction.
    var quantite by remember { mutableStateOf(0) }

    // TODO B — FAIT : état de sélection, même principe.
    var selectionnee by remember { mutableStateOf(false) }

    // Couleur calculée ici (même portée que le Log.i ci-dessus) : lire
    // selectionnee à cet endroit précis explique pourquoi un clic sur la
    // carte déclenche une recomposition de TOUTE la fonction, contrairement
    // à quantite qui n'est lu que plus bas, à l'intérieur de la Card.
    val couleurCarte = if (selectionnee)
        MaterialTheme.colorScheme.primaryContainer
    else MaterialTheme.colorScheme.surfaceVariant

    Card(
        modifier = Modifier
            .fillMaxWidth()
            // .clickable AVANT .padding : la zone de marge fait aussi
            // partie de la zone cliquable (l'ordre des Modifier compte).
            .clickable {
                selectionnee = !selectionnee
                Log.i("ACTION", "Clic CARTE -> selectionnee = $selectionnee")
            }
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = couleurCarte),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(produit.nom, style = MaterialTheme.typography.titleLarge)
            Text(
                "Origine : ${produit.origine}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                produit.prixKg?.let { "${formatAriary(it)} / kg" } ?: "prix non fixé",
                style = MaterialTheme.typography.bodyLarge,
            )

            Spacer(Modifier.height(12.dp))

            // TODO A — FAIT :
            Text("Quantité : $quantite kg")
            Button(onClick = {
                quantite++
                Log.i("ACTION", "Clic BOUTON -> quantite = $quantite")
            }) { Text("Ajouter 1 kg") }
        }
    }
}

/** Formate un montant en ariary : 1250000.0 -> "1 250 000 Ar" (repris du mini-TP 1). */
fun formatAriary(montant: Double): String {
    val entier = montant.toLong().toString()
    val groupes = entier.reversed().chunked(3).joinToString(" ").reversed()
    return "$groupes Ar"
}