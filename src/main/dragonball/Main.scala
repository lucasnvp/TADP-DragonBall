package main.dragonball

import main.dragonball.Criterio.MayorDanioAlEnemigo
import main.dragonball.Item._

object Main extends App{
  var mrSatan = Guerrero("MrSatan", Especie.Humano, List(Movimiento.dejarseFajar), null, 100, 100)
  println(mrSatan)
  var goku = Guerrero("Goku", Especie.Saiyajin(), List(Movimiento.cargarKi, Movimiento.muchosGolpesNinja, Movimiento.ondaDeEnergia(5)), null, 500, 1000)
  println(goku)
//  var gokuVsmrSatan = (goku, mrSatan)
//  println(gokuVsmrSatan)
//
//  gokuVsmrSatan = goku.atacar(Movimiento.cargarKi, mrSatan)
//  println(gokuVsmrSatan)
//
//  gokuVsmrSatan = goku.atacar(Movimiento.usarItem(SemillaDeErmitanio), mrSatan)
//  println(gokuVsmrSatan)
//
//  gokuVsmrSatan = goku.atacar(Movimiento.usarItem(Roma), mrSatan)
//  println(gokuVsmrSatan)

  // todo test arma filosa

  // todo test arma fuego

  // todo test comer oponente

//  println("Movimiento mas efectivo")
//  println(goku.movimientoMasEfectivoContra(mrSatan)(MayorDanioAlEnemigo))

  println("Plan de ataque")
  println(goku.planDeAtaqueContra(mrSatan, 2)(MayorDanioAlEnemigo))

}