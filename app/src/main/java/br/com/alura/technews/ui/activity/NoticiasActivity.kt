package br.com.alura.technews.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.alura.technews.R
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.ui.fragment.ListaNoticiasFragment
import br.com.alura.technews.ui.fragment.VisualizaNoticiaFragment

private const val TITULO_APPBAR = "Notícias"

class NoticiasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticias)
        title = TITULO_APPBAR
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.activity_noticias_conteiner, ListaNoticiasFragment())
        transaction.commit()
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        if (fragment is ListaNoticiasFragment) {
            fragment.quandoNoticiaSelecionada = { noticia ->
                abreVisualizadorNoticia(noticia)
            }
            fragment.quandoFabSalvaNoticiasClicado = {
                abreFormularioModoCriacao()
            }
        }
        if (fragment is VisualizaNoticiaFragment) {
            fragment.quandoFinish = { finish() }
            fragment.quandoAbreFormularioEdicao = { noticia -> abreFormularioEdicao(noticia) }
        }

    }

    private fun abreFormularioModoCriacao() {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        startActivity(intent)
    }

    private fun abreVisualizadorNoticia(noticia: Noticia) {
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = VisualizaNoticiaFragment()
        val bundle = Bundle()
        bundle.putLong(NOTICIA_ID_CHAVE, noticia.id)
        fragment.arguments = bundle
        transaction.replace(R.id.activity_noticias_conteiner, fragment)
        transaction.commit()
    }

    private fun abreFormularioEdicao(noticia: Noticia) {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        intent.putExtra(NOTICIA_ID_CHAVE, noticia.id)
        startActivity(intent)
    }

}
