package main.dragonball

object Especie {
  sealed trait Especie

  case object Humano extends Especie

  case class Saiyajin(cola: Boolean = true, estado: Option[Transformacion] = None) extends Especie {

    def cortarCola = copy(cola = false)

    def cambiarEstado(otroEstado: Transformacion) = copy(estado = Some(otroEstado))

    def nivel = {
      estado match {
        case Some(SuperSaiyajin(nivel)) => nivel
        case _ => 0
      }
    }

  }

  case object Androide extends Especie

  case object Namekusein extends Especie

  case class Monstruo() extends Especie {
    // todo formas de digerir
    def comer(atacante: Guerrero, oponente: Guerrero) = {
      (atacante, oponente.setKi(0).cambioDeEstado(Muerto))
    }
  }

  case class Fusion(guerrero: Guerrero) extends Especie

}
