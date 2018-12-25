# Dragon Ball TP
#### TADP - 2018 C2 - TP Objeto/Funcional http://dragonball.wikia.com

###### Introducción

En el universo de Dragon Ball, existen distintos guerreros que combaten entre sí y poseen habilidades y técnicas que permiten ayudarlos en el combate para vencer a sus oponentes. Para ayudarlos a mejorar en su entrenamiento y elaborar mejores estrategias de batalla vamos a diseñar un sistema que simule los combates y nos permita anticiparnos a los resultados.

IMPORTANTE: Este trabajo práctico debe implementarse de manera que se apliquen los principios del paradigma híbrido objeto-funcional enseñados en clase. No alcanza con hacer que el código funcione en objetos, hay que aprovechar las herramientas funcionales, poder justificar las decisiones de diseño y elegir el modo y lugar para usar conceptos de un paradigma u otro.
Se tendrán en cuenta para la corrección los siguientes aspectos:
Uso de Inmutabilidad vs. Mutabilidad.
Uso de Polimorfismo paramétrico (Pattern Matching) vs. Polimorfismo Ad-Hoc.
Aprovechamiento del polimorfismo entre objetos y funciones y Orden Superior.
Uso adecuado del sistema de tipos.
Manejo de herramientas funcionales.
Cualidades de Software.
Diseño de interfaces y elección de tipos.


## Descripción General del Dominio
### Guerreros
Los guerreros son el centro del universo de Dragon Ball. Todos los guerreros tienen un nombre, un inventario con los ítems que tienen disponibles y una cierta cantidad de energía espiritual llamada “ki” que representa su poder físico y bienestar general.

De cada guerrero se conoce también su especie, la cual influencia profundamente su desempeño y habilidades:

- Humanos: Los humanos son humanos. Qué querés que te diga?


- Saiyajins: Los saiyajins son seres similares a humanos, pero son en general más fuertes y poseen una cola que les da poderes sobrenaturales. Además tienen el poder de entrar temporalmente en un estado de exaltación conocido como “Super Saiyajin”, durante el cual sus poderes se multiplican.


- Androides: Los androides son robots diseñados para pelear. A diferencia de los guerreros orgánicos no poseen ki, no necesitan comer y no pueden nunca quedar inconscientes. Además cuentan con una batería interna que les permite absorber y almacenar energía absorbiendo cierto tipo de ataques.


- Namekuseins: Los namekuseins son extraterrestres verdes con extraordinarios poderes curativos.


- Monstruos: Son criaturas malvadas y extremadamente fuertes que pueden comerse a sus oponentes para adquirir sus habilidades y poderes.

### Movimientos
Los guerreros tienen a su disposición una gran variedad de habilidades y poderes que pueden utilizar para atacarse, protegerse y/o curarse unos a otros. Estas habilidades pueden aprenderse de varias formas y, una vez adquiridas, pueden ser usadas en cualquier momento durante el combate (siempre y cuando se cumplan los requerimientos).

- Dejarse Fajar: No todo es gloria. Un guerrero puede decidir quedarse quietito, así, sin hacer nada.

- Cargar Ki: Los guerreros pueden detenerse durante el combate a juntar fuerzas para seguir. Cuando un guerrero hace esto su ki aumenta 100 puntos, a menos que sea un Super Saiyajin, los cuales aumentan 150 puntos por cada nivel de Super Saiyan. Los androides, cuyo ki es nulo, no ganan nada al realizar este ataque.

- Usar Ítem: Los guerreros pueden ir equipados con diversos elementos. Para poder realizar un movimiento que use un ítem sobre el oponente o sí mismo, el guerrero debe tener dicho ítem en su inventario, de lo contrario el movimiento no producirá ningún efecto. Algunos de los ítems son:

    - Armas: Pueden ser romas, filosas o de fuego.
    
        Las armas romas dejan inconsciente a cualquier oponente no-androide con menos de 300 de ki.

        Las armas filosas reducen el ki del oponente en un punto por cada 100 del atacante. Además, si un Saiyajin con cola es atacado por un arma filosa, la pierde. Esto causa que su ki se reduzca automáticamente a 1 y, si estaba convertido en Mono Gigante, vuelva a su forma normal y quede inconsciente.

        Las armas de fuego son, en general, muy débiles para afectar a la mayoría de los guerreros. Sólo le causan 20 puntos de ki a los humanos, o 10 a Namekuseins inconscientes. Además, para poder usarlas el atacante debe tener munición adecuada para dicha arma, de la cual se gastará una unidad.

    - Semilla del Ermitaño: Son semillas mágicas que recuperan a aquel que las consume a su máximo potencial. Comer una de estas semillas hace que cualquier guerrero recupere su ki máximo. Aun así, no hace que las colas cortadas vuelvan a crecer.

- Comerse al Oponente: Como ya se mencionó, los monstruos pueden comerse a sus oponentes y chuparles los jugos para ganar poderes increíbles. Cada monstruo “digiere” a sus oponentes de una forma diferente (Ej: Cell sólo puede digerir Androides, pero cuando lo hace, aprende los movimientos de todos ellos; mientras que Majin Buu puede comerse a cualquiera, pero sólo recuerda los poderes del último guerrero que se comió).

    Cuando un monstruo se come a su oponente, aprende los movimientos de acuerdo a su forma de digerir y su oponente muere.

    Sólamente los monstruos pueden comer gente (y sólo pueden comerse enemigos con menor ki que ellos). Cualquier otro guerrero que lo intente no obtiene ningún resultado más que pasar un poco de vergüenza.


- Convertirse en Mono: Los Saiyajins que tengan cola y una foto de la luna en su inventario pueden convertirse en un mono gigante. Cuando esto pasa, su ki máximo se triplica y recuperan todo el ki perdido instantáneamente. Esta habilidad no puede combinarse con transformarse en Super Saiyajin.
Si el mono gigante pierde su cola vuelve a su estado normal.


- Convertirse en Super Saiyajin: Cuando un Saiyajin se vuelve muy poderoso se convierte en Super Saiyajin, estas transformaciones son acumulables (eso quiere decir que cuando un SS se vuelve muy fuerte se puede convertir en SS nivel 2, luego en SS nivel 3 y así...). Para poder convertirse en SS o pasar al siguiente nivel, el ki del Saiyajin debe estar, por lo menos, por la mitad de su máximo actual.
Al transformarse, el máximo ki del guerrero se multiplica por 5 por cada nivel de Super Saiyajin, pero su ki no aumenta. Si el guerrero queda inconsciente o se transforma en mono el estado de SS se pierde.


- Fusión: Fusionarse es una de las transformaciones más poderosas en el universo de Dragon Ball. Cualquier humano, saiyajin o namekusei puede fusionarse con un amigo (otro guerrero de cualquiera de esas especies) para convertirse en una nueva forma de Guerrero que sustituye al peleador original en su combate. El guerrero resultante de la fusión gana todos los movimientos de los dos que lo originaron, y la suma de ki y ki máximo de ambos.

    El peleador fusionado no se considera de ninguna especie (no tiene ninguna ventaja ni desventaja de ellas) y no puede a su vez fusionarse con otro. Si queda inconsciente o muerto, es reemplazado por el peleador original, también inconsciente o muerto.


- Magia: Muchos guerreros tienen poderes místicos que les permiten jugar con las reglas de la física y la lógica. Cualquiera de ellos puede realizar un pase de magia que cambie su estado y/o el de su oponente de alguna forma arbitraria.
Esta habilidad sólo puede ser realizada por Namekuseins, monstruos y guerreros que tengan 7 esferas del dragon en su inventario (las cuales se pierden luego de usarse).

- Ataques: Llamamos “Ataque” a cualquier movimiento que no requiere de accesorios especiales y tiene como único fin causar daño al enemigo (lo cual se manifiesta reduciendo su ki o batería en el caso de los androides). Estos movimientos pueden ser clasificados en ataques “Físicos” o “De Energía”.

    Los androides que son atacados con energía, en lugar de perder batería, la ganan (teniendo en cuenta que nunca pueden ganar más batería que su máximo).

    Siempre que el ki (o batería) de un oponente se reduce a cero como resultado de un ataque, este muere (El valor nunca puede ser menor a cero).

    Algunos de los ataques conocidos son:

    - Muchos Golpes Ninja (Físico): Los guerreros pueden cruzar golpes a mucha velocidad, pero no siempre termina bien. El peleador con menor ki (ya sea el atacante o el atacado) sufre 20 puntos de daño, a menos que el atacante sea humano y el atacado androide, en cuyo caso sólo el humano pierde 10 puntos de ki, porque los androides son duros y se lastima los deditos.

    - Explotar (Físico): Tal vez no parezca un gran plan, pero quién soy yo para juzgar? Cuando un guerrero realiza este ataque pierde todo su ki/batería y muere automáticamente pero su oponente sufre el doble del ki (o el triple de la batería) que tenía el atacante.
Los namekuseins, debido a su gran elasticidad, no pueden morir como resultado de recibir este ataque (su ki baja como mínimo a 1).
Sólo los monstruos y los androides pueden explotar.

    - Onda (De Energía): Este tipo de ataque viene en varios sabores (Kame Hame Ha, Kienzan, Dodonpa, etc.) y básicamente consiste en juntar una pelota de ki y tirarsela al oponente.
Cada onda requiere de gastar cantidad de ki para poder ser realizada y causa el doble de ese ki de daño al oponente (pero sólo la mitad si es un monstruo). Si el atacante no posee suficiente ki para la onda no puede hacer el ataque.
Para estos ataques, los Androides usan su batería en lugar de su ki.

    - Genkidama (De Energía): Este ataque es en muchos sentidos similar a una Onda de Energía, pero mucho más grande y creada por ki externo al atacante. Para lanzar una genkidama el guerrero debe juntar energía de su entorno ya que, cuanto más junte, más poderoso será el ataque.
Esta habilidad causa 10 puntos de daño elevado a la cantidad de rounds que el guerrero haya pasado dejandose fajar inmediatamente antes (esto quiere decir que si se dejó fajar el round anterior el ataque hará 10, pero si se dejó fajar los últimos 4 turnos hará 10000!).
Si el atacante realiza otro movimiento o queda muerto o inconsciente, la energía se dispersa y hay que empezar a juntar de nuevo.

Nota: Ninguno de los movimientos previamente descritos pueden ser usados por guerreros muertos o inconscientes, con la excepción de comerse una Semilla del Ermitaño que puede ser realizado incluso por guerreros inconscientes (se la metés en la boca y ya). Si un guerrero en estas condiciones intenta realizar un movimiento simplemente no tendrá ningún efecto; sin embargo, eso no evita que su oponente contraataque…

### Requerimientos

Se pide implementar los siguientes casos de uso, acompañados de sus correspondientes tests y la documentación necesaria para explicar su diseño (la cual debe incluir, mínimo, un diagrama de clases):

##### 1. Movimiento Más Efectivo

Un guerrero debe poder seleccionar de entre sus movimientos disponibles aquel que resulte el mejor (de acuerdo a algún criterio) para realizar contra cierto oponente.

```scala
val goku : Guerrero = ???
val vegeta : Guerrero = ???
val unCriterio: Criterio = ???

goku.movimentoMasEfectivoContra(vegeta)(unCriterio)
```

Los criterios pueden ser de lo más variados: mientras que algunos guerreros podría querer usar el movimiento que más daño le haga al enemigo, otros, que disfrutan peleando contra oponentes fuertes, podrían preferir el movimiento que deje a su enemigo con la mayor cantidad de ki. Los más tacaños podrían querer usar los movimientos que les hagan perder la menor cantidad de ítems, mientras que Krillin podría conformarse con cualquier movimiento que no lo mate.
Para simplificar la decisión, se pide modelar los criterios como cuantificadores del resultado de realizar el movimiento; es decir, un criterio debe procesar el estado del atacante y el defensor luego de realizar el movimiento y producir un número que represente qué tan “deseado” es dicho resultado. Cuanto más grande sea el número, más favorecido será el movimiento analizado. Si el resultado del criterio es igual o menor a 0 significa que el movimiento no es deseable en absoluto y no debe ser considerado una respuesta válida.

De más está decir que la elección del movimiento no debe producir ningún efecto colateral sobre ninguno de los guerreros involucrados.

Es importante tener en cuenta que el guerrero podría no disponer de ningún movimiento que satisfaga el criterio, lo cual debe ser manejado de forma acorde. En caso de que el criterio produzca el mismo valor para más de un movimiento, se puede elegir cualquier de ellos.
 

##### 2. Pelear un Round

Como era de esperarse, el sistema debe poder predecir el resultado de un intercambio de golpes entre guerreros.

Cuando un guerrero pelea un round, realiza un movimiento (previamente elegido) contra el oponente. Por supuesto, el oponente no se queda de brazos cruzados sino que, inmediatamente después de sufrir el efecto del movimiento, realiza un contraataque.

Anticipándose al peor escenario, el sistema debe asumir que el oponente siempre realiza el ataque que lo deje con la mayor ventaja (o al menos, con la menor desventaja) sobre los puntos de ki.

```scala
val goku : Guerrero = ???
val vegeta : Guerrero = ???
val movimientoDeGoku: Movimiento = ???

goku.pelearRound(movimientoDeGoku)(vegeta)
```

Al finalizar el round, el usuario debe poder tener acceso al nuevo estado del atacante y el defensor.


##### 3. Plan de Ataque

Durante un combate es importante tener una estrategia. Dado un guerrero y un criterio de selección de movimientos, el sistema debe permitir elaborar un plan de acción, consistente en una secuencia de movimientos, para pelear contra un oponente durante cierta cantidad determinada de rounds.

```scala
val goku : Guerrero = ???
val vegeta : Guerrero = ???
val cantidadDeRounds: Int = ???
val unCriterio: Criterio = ???

goku.planDeAtaqueContra(vegeta,cantidadDeRounds)(unCriterio)
```

Para cada round solicitado, el sistema debe elegir el movimiento más efectivo (de acuerdo al criterio recibido) para realizar contra el oponente. Luego debe simular la pelea de dicho round utilizando el movimiento elegido y seleccionar el movimiento para el siguiente round basándose en el resultado de este.

Ej: Yajirobe elabora un plan de ataque para 2 rounds contra un monstruo, utilizando un criterio que favorece los resultados que lo dejan con mayor ki. Para el primer round, dado que su ki está al máximo y le da igual usar cualquier movimiento, elige usar su espada para atacar al monstruo; pero luego del contraataque su ki va a haber bajado, así que para el segundo round elige comerse una Semilla del Ermitaño para restaurarse.

Tener en cuenta que de nada sirve un plan de ataque incompleto. Si el guerrero no encuentra un movimiento satisfactorio para cada round pedido, NO DEBE retornarse un plan más corto.


##### 3-b. BONUS! (opcional)

Hacer el punto 3 sin usar recursividad ni asignación destructiva!


##### 4. A Pelear!

Dado un guerrero y un plan de ataque, llevar a cabo una pelea contra un oponente y obtener el resultado.

```scala
val goku : Guerrero = ???
val vegeta : Guerrero = ???
val elPlanDeGoku: PlanDeAtaque = ???

val resultado: ResultadoPelea = goku.pelearContra(vegeta)(elPlanDeGoku)
```

Los resultados posibles para una pelea son:

- Uno de los dos peleadores ganó: Esto ocurre solamente cuando el oponente muere. Obviamente, si este es el caso, queremos saber quién de los dos fué y en qué estado quedó. El estado del perdedor no importa.

- Ambos siguen peleando: La pelea no tiene un ganador definido, pero queremos saber en qué estado están.

Tener en cuenta que, para ganar, no es necesario pelear todos los rounds previstos en el plan. Si uno de los peleadores muere durante un round, ese round se termina de pelear, se declara al ganador y ya NO DEBEN PELEARSE LOS ROUNDS SIGUIENTES; es decir, el estado del ganador es el que le quede en el round que ganó. Si ambos peleadores mueren en el mismo round se considera que el ganador es el receptor del mensaje (después de todo, cumplió el objetivo de su ataque...).
