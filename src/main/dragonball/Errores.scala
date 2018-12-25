package main.dragonball

import main.dragonball.Movimiento.Movimiento

class InvalidAttackException(str: String) extends RuntimeException
class MovimientoMasEfectivoException(str: String) extends RuntimeException
class PlanDeAtaqueException(list: List[Movimiento]) extends RuntimeException
