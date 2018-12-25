package main.dragonball

import main.dragonball.Criterio.{Criterio, MayorDanioAlEnemigo}
import main.dragonball.Especie.Especie
import main.dragonball.Item.Item
import main.dragonball.Movimiento.{Movimiento, Peleadores}

import scala.util.{Failure, Success, Try}

case class Guerrero(nombre: String,
                    especie: Especie,
                    habilidades: List[Movimiento],
                    inventario: List[Item],
                    ki: Int = 0,
                    kiMax: Int,
                    estado: Estado = Normal){

  def cambioDeEstado(nuevoEstado: Estado) :Guerrero = copy(estado = nuevoEstado)
  def setKiMaximo(ki: Int) :Guerrero = copy(kiMax = ki)
  def setKi(cant: Int) :Guerrero = copy(ki = cant)
  def setEspecie(unaEspecie: Especie) :Guerrero = copy(especie = unaEspecie)
  def setInventario(nuevoInventario: List[Item]) :Guerrero = copy(inventario = nuevoInventario)

  def incrementarKi(cant: Int) :Guerrero = copy(ki = (ki + cant).max(kiMax))

  def decrementarKi(cant: Int) :Guerrero = {
    if ((ki - cant) <= 0) {copy(estado = Muerto)}
    else { copy(ki = ki - cant) }
  }

  def usarItem(unItem: Item, itemUsado: Item) :Guerrero = copy(inventario = inventario.updated(inventario.indexOf(unItem), itemUsado))

  def atacar(movimiento: Movimiento, oponente: Guerrero) :(Guerrero,Guerrero) = {
    movimiento(this, oponente)
  }

  def movimientoMasEfectivoContra(oponente: Guerrero)(criterio: Criterio) :Option[Movimiento] = {
    criterio match {
      case MayorDanioAlEnemigo => Some(Criterio.mayorDanioAlEnemigo(this, oponente))
      case _ => None
    }
  }

  def pelarRound(movimiento: Movimiento)(oponente: Guerrero) :(Guerrero, Guerrero) ={
    var peleadores = this.atacar(movimiento, oponente)
    val atacante = peleadores._1
    val oponenteAtacado = peleadores._2
    val oponenteMovimiento = oponenteAtacado.movimientoMasEfectivoContra(atacante)(MayorDanioAlEnemigo) match {
      case Some(movimientoSeleccionado) => movimientoSeleccionado
      case None => null
    }
    peleadores = oponenteAtacado.atacar(oponenteMovimiento, atacante)
    peleadores
  }

  def planDeAtaqueContra(oponente: Guerrero, cantidadDeRounds: Int)(criterio: Criterio) :Try[List[Movimiento]] = {
    val round = List.fill(cantidadDeRounds) {
      (atacanteRecursivo: Guerrero, oponenteRecursivo: Guerrero, plan: List[Movimiento]) =>
        val movientoMasEfectivo = atacanteRecursivo.movimientoMasEfectivoContra(oponenteRecursivo)(MayorDanioAlEnemigo) match {
          case Some(movimientoSeleccionado) => movimientoSeleccionado
          case None => throw new MovimientoMasEfectivoException("No hay movimiento efectivo")
        }
        val (atacanteNuevo, oponenteNuevo) = atacanteRecursivo.pelarRound(movientoMasEfectivo)(oponenteRecursivo)
        (atacanteNuevo, oponenteNuevo, plan:+ movientoMasEfectivo)
    }
    val (_,_,planDeAtaque) = round.foldLeft((this, oponente, List[Movimiento]())){
      case ((atc, opt, plan), round) => round(atc, opt, plan)
    }
    if(planDeAtaque.size == cantidadDeRounds)
      Try(planDeAtaque)
    else
      Try(throw new PlanDeAtaqueException(planDeAtaque))
  }

  def pelearContra(oponente: Guerrero)(planDeAtaque: List[Movimiento]) :ResultadoPelea = {
    val peleadores = PeleaEnCurso(this, oponente)
    val resultadoPelea = try planDeAtaque.foldLeft(peleadores) { (peleadores, movimiento) =>
      val peleadoresPeleanRound = peleadores.atacante.pelarRound(movimiento)(peleadores.oponente)
      val estadoPeleadores = (peleadoresPeleanRound._1.estado, peleadoresPeleanRound._2.estado)
      estadoPeleadores match {
        case (_,Muerto) => for (peleadores <- peleadoresPeleanRound) yield PeleaGanada(peleadoresPeleanRound._1)
        case (Muerto,_) => for (peleadores <- peleadoresPeleanRound) yield PeleaGanada(peleadoresPeleanRound._2)
      }
      peleadores
    }
    resultadoPelea match {
      case Success(_) => resultadoPelea
      case Failure(e) => PeleaCancelada(e.getMessage)
    }
  }

}

sealed trait Estado
// todo si esta fusionado vuelve al peleador anterior
case object Muerto extends Estado
// todo Si es SuperSaiyajin lo pierte
// todo Si esta fusionado vuelve al peleador anterior
case object Inconsciente extends Estado
case object Normal extends Estado

sealed trait Transformacion
case class SuperSaiyajin(nivel: Int) extends Transformacion
case class Mono() extends Transformacion

sealed trait ResultadoPelea
case class PeleaEnCurso(atacante: Guerrero, oponente: Guerrero) extends ResultadoPelea
case class PeleaGanada(atacante: Guerrero) extends ResultadoPelea
case class PeleaCancelada(error: String) extends ResultadoPelea