package main.dragonball

import main.dragonball.Especie._
import main.dragonball.Item._

object Movimiento {
  type Peleadores = (Guerrero, Guerrero)
  type Movimiento = (Guerrero, Guerrero) => (Guerrero, Guerrero)

  case object dejarseFajar extends Movimiento{
    def apply(atacante: Guerrero, oponente: Guerrero) :(Guerrero, Guerrero) = (atacante, oponente)
  }

  case object cargarKi extends Movimiento{
    def apply(atacante: Guerrero, oponente: Guerrero) :(Guerrero, Guerrero) = {
      atacante.especie match {
        case Androide => throw new InvalidAttackException("Los androides no cargan ki")
        case Saiyajin(_, estado) => estado match {
          case Some(SuperSaiyajin(nivel)) => (atacante.incrementarKi(150 * nivel),oponente)
          case _ => (atacante.incrementarKi(100),oponente)
        }
        case _ => (atacante.incrementarKi(100), oponente)
      }
    }
  }

  case class usarItem(item: Item) extends Movimiento{
    def apply(atacante: Guerrero, oponente: Guerrero) :(Guerrero, Guerrero) = {
      item match {
        case Roma => oponente.especie match {
          case Androide => (atacante, oponente)
          case _ =>
            if (oponente.ki < 300) (atacante, oponente.cambioDeEstado(Inconsciente))
            else (atacante, oponente)
        }
        case Filosa => oponente.especie match {
          case Saiyajin(true, estado) => estado match {
            case Some(Mono()) => (atacante, oponente
              .setEspecie(Saiyajin(false, None))
              .cambioDeEstado(Inconsciente)
              .setKi(1))
            case _ => (atacante, oponente.setEspecie(Saiyajin(false, estado)).setKi(1))
          }
          case _ => (atacante, oponente.setKi((atacante.ki/100) * -1))
        }
        case Fuego(municion) if municion > 0 =>
          val atacanteUsoItem = atacante.usarItem(item, Fuego(municion - 1))
          oponente.especie match {
          case Humano => (atacanteUsoItem, oponente.decrementarKi(20))
          case Namekusein => oponente.estado match {
            case Inconsciente => (atacanteUsoItem, oponente.decrementarKi(10))
            case _ => (atacanteUsoItem, oponente)
          }
          case _ => (atacanteUsoItem, oponente)
          }
        case SemillaDeErmitanio => (atacante.setKi(atacante.kiMax), oponente)
        case _ => (atacante, oponente)
      }
    }
  }

  case object comerOponente extends Movimiento{
    def apply(atacante: Guerrero, oponente: Guerrero) :(Guerrero, Guerrero) = {
      atacante.especie match {
        case esMonstruo: Monstruo =>
          if (atacante.ki > oponente.ki) esMonstruo.comer(atacante, oponente)
          else (atacante, oponente)
        case _ => (atacante, oponente)
      }
    }
  }

  case object convertirseEnMono extends Movimiento{
    def apply(atacante: Guerrero, oponente: Guerrero) :(Guerrero, Guerrero) = {
      atacante.especie match {
        case Saiyajin(_, Some(Mono())) => throw new InvalidAttackException("Ya es un mono")
        case Saiyajin(true, _) if atacante.inventario.contains(FotoDeLaLuna) =>
          (atacante.setKiMaximo(atacante.kiMax * 3).setKi(atacante.kiMax).setEspecie(Saiyajin(true, Some(Mono()))), oponente)
        case _ => (atacante, oponente)
      }
    }
  }

  case object convertirseEnSS extends Movimiento{
    def apply(atacante: Guerrero, oponente: Guerrero) :(Guerrero, Guerrero) = {
      atacante.especie match {
        case Saiyajin(cola, estado) if atacante.ki > (atacante.kiMax/2) => estado match {
          case Some(Mono()) => throw new InvalidAttackException("Un mono no se puede transformar en super saiyajin")
          case None =>
            (atacante.setKiMaximo(atacante.kiMax*5).setEspecie(Saiyajin(cola,Some(SuperSaiyajin(1)))), oponente)
          case Some(SuperSaiyajin(nivel)) =>
            (atacante.setKiMaximo(atacante.kiMax * 5 * nivel).setEspecie(Saiyajin(cola,Some(SuperSaiyajin(nivel + 1))))
              , oponente)
          case _ => (atacante, oponente)
        }
        case _ => (atacante, oponente)
      }
    }
  }

  case class fusion(otroGuerrero: Guerrero) extends Movimiento{
    def apply(atacante: Guerrero, oponente: Guerrero) :(Guerrero, Guerrero) = {
      atacante.especie match {
        case Humano | Namekusein | Saiyajin(_,_) =>
          otroGuerrero.especie match {
            case Humano | Namekusein | Saiyajin(_, _) => otroGuerrero
            case _ => throw new InvalidAttackException("Error al fusionarse el otro guerrero no es valido")
          }
          val guerreroFuionado = atacante.copy(
            especie = Fusion(atacante),
            habilidades = atacante.habilidades ++ otroGuerrero.habilidades,
            ki = atacante.ki + otroGuerrero.ki,
            kiMax = atacante.kiMax + otroGuerrero.kiMax
          )
          (guerreroFuionado, oponente)
        case _=> (atacante, oponente)
      }
    }
  }

  case class magia(paseMagico: (Guerrero, Guerrero) => (Guerrero, Guerrero)) extends Movimiento{
    def apply(atacante: Guerrero, oponente: Guerrero) :(Guerrero, Guerrero) = {
      if (tieneEsferasDelDragon(atacante)) {
        perderEsferasDelDragon(atacante)
        paseMagico(atacante, oponente)
      }
      else (atacante, oponente)
    }
    def tieneEsferasDelDragon(guerrero: Guerrero) :Boolean = {
      guerrero.inventario.count(e => e.equals(EsferaDeDragon)).equals(7)
    }

    def perderEsferasDelDragon(guerrero :Guerrero) :Guerrero = {
      val lista = guerrero.inventario.filter(e => ! e.equals(EsferaDeDragon))
      guerrero.setInventario(lista)
    }
  }

  case object muchosGolpesNinja extends Movimiento{
    def apply(atacante: Guerrero, oponente: Guerrero) :(Guerrero,Guerrero) = {
      atacante.especie match {
        case Humano => oponente.especie match {
          case Androide => (atacante.decrementarKi(10), oponente)
          case _ =>
            if(atacante.ki > oponente.ki) {(atacante, oponente.decrementarKi(20))}
            else (atacante.decrementarKi(20), oponente)
        }
        case _ =>
          if(atacante.ki > oponente.ki) {(atacante, oponente.decrementarKi(20))}
          else (atacante.decrementarKi(20), oponente)
      }
    }
  }

  case object explotar extends Movimiento{
    def apply(atacante: Guerrero, oponente: Guerrero) :(Guerrero,Guerrero) = {
      val oponenteDaniado: Guerrero = atacante.especie match {
        case Androide => oponente.decrementarKi(atacante.ki * -3)
        case Monstruo() => oponente.decrementarKi(atacante.ki * -2)
        case _ => throw new InvalidAttackException("Solo los androides y monstruos pueden explotar")
      }
      val validarOponenteDaniado: Guerrero = oponenteDaniado.especie match {
        case Namekusein =>
          if (oponenteDaniado.ki == 0) oponenteDaniado.setKi(1).cambioDeEstado(Normal)
          else oponenteDaniado
        case _ => oponenteDaniado
      }
      (atacante.setKi(0).cambioDeEstado(Muerto),validarOponenteDaniado)
    }
  }

  case class ondaDeEnergia(kiRequerido: Int) extends Movimiento{
    def apply(atacante: Guerrero, oponente: Guerrero) :(Guerrero,Guerrero) = {
      if (atacante.ki >= kiRequerido) {
        val oponenteDaniado: Guerrero = oponente.especie match {
          case Monstruo() => oponente.decrementarKi(kiRequerido/2)
          case _ => oponente.decrementarKi(kiRequerido*2)
        }
        (atacante.decrementarKi(kiRequerido), oponenteDaniado)
      }
      else (atacante, oponente)
    }
  }

  case object Genkidama extends Movimiento{
    def apply(atacante: Guerrero, oponente: Guerrero) :(Guerrero,Guerrero) = (atacante, oponente)
  }

}