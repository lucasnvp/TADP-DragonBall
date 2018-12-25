package main.dragonball

object Criterio {
  sealed trait Criterio
  case object MayorDanioAlEnemigo extends Criterio
  case object MayorCantidadDeKi extends Criterio
  case object Tacanio extends Criterio
  case object CualquierMovientoQueNoLoMate extends Criterio

  def mayorDanioAlEnemigo(atacante: Guerrero, oponente: Guerrero)= {
    val resultados = for {
      movimiento <- atacante.habilidades
      oponenteAtacado = atacante.atacar(movimiento, oponente)
      valor = oponente.ki - oponenteAtacado._2.ki
    }yield (movimiento, valor)

    resultados.sortBy(_._2).reverse.head._1
  }
}

