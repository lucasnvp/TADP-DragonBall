package main.dragonball

object Item {
  sealed trait Item
  sealed trait Arma extends Item
  case object SemillaDeErmitanio extends Item
  case object Filosa extends Arma
  case object Roma extends Arma
  case class Fuego(municion: Int) extends Arma
  case object FotoDeLaLuna extends Item
  case object EsferaDeDragon extends Item
}
