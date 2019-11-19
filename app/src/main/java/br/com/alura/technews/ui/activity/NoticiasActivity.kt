package br.com.alura.technews.ui.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.alura.technews.R
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.ui.activity.extensions.transacaoFragment
import br.com.alura.technews.ui.fragment.ListaNoticiasFragment
import br.com.alura.technews.ui.fragment.VisualizaNoticiaFragment
import kotlinx.android.synthetic.main.activity_noticias.*

private const val TAG_FRAGMENT_VISUALIZA_NOTICIA = "visualizaNoticia"

class NoticiasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticias)
        if (savedInstanceState == null) {
            abreListaNoticias()
        } else {
            supportFragmentManager
                .findFragmentByTag(TAG_FRAGMENT_VISUALIZA_NOTICIA)
                ?.let { fragment ->
                    val arguments = fragment.arguments
                    val visualizaNoticiaFragment = VisualizaNoticiaFragment()
                    visualizaNoticiaFragment.arguments = arguments
                    transacaoFragment {
                        remove(fragment)
                    }
                    supportFragmentManager.popBackStack()
                    transacaoFragment {
                        val container =
                            if (activity_noticias_conteiner_secundario != null) {
                                R.id.activity_noticias_conteiner_secundario
                            } else {
                                addToBackStack(null)
                                R.id.activity_noticias_conteiner_primario
                            }
                        replace(container, visualizaNoticiaFragment, TAG_FRAGMENT_VISUALIZA_NOTICIA)
                    }
                }
        }
    }

    private fun abreListaNoticias() {
        transacaoFragment {
            add(R.id.activity_noticias_conteiner_primario, ListaNoticiasFragment())
        }
    }

    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
        when (fragment) {
            is ListaNoticiasFragment -> {
                configuraListaNoticias(fragment)
            }
            is VisualizaNoticiaFragment -> {
                configuraVisualizaNoticias(fragment)
            }
        }
    }

    private fun configuraVisualizaNoticias(fragment: VisualizaNoticiaFragment) {
        fragment.quandoFinish = this::finish
        fragment.quandoAbreFormularioEdicao = this::abreFormularioEdicao
    }

    private fun configuraListaNoticias(fragment: ListaNoticiasFragment) {
        fragment.quandoNoticiaSelecionada = this::abreVisualizadorNoticia
        fragment.quandoFabSalvaNoticiasClicado = this::abreFormularioModoCriacao
    }

    private fun abreFormularioModoCriacao() {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        startActivity(intent)
    }

    private fun abreVisualizadorNoticia(noticia: Noticia) {
        val fragment = VisualizaNoticiaFragment()
        val bundle = Bundle()
        bundle.putLong(NOTICIA_ID_CHAVE, noticia.id)
        fragment.arguments = bundle
        transacaoFragment {
            val container =
                if (activity_noticias_conteiner_secundario != null) {
                    R.id.activity_noticias_conteiner_secundario
                } else {
                    addToBackStack(null)
                    R.id.activity_noticias_conteiner_primario
                }
            replace(container, fragment, TAG_FRAGMENT_VISUALIZA_NOTICIA)
        }
    }

    private fun abreFormularioEdicao(noticia: Noticia) {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        intent.putExtra(NOTICIA_ID_CHAVE, noticia.id)
        startActivity(intent)
    }

}
