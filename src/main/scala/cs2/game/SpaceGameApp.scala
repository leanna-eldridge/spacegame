package cs2.game

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.image.Image
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.paint.Color
import scalafx.animation.AnimationTimer
import scalafx.scene.input.KeyCode
import scalafx.scene.input.KeyEvent
import scala.util.Random
import scala.collection.mutable.Buffer
import scala.collection.mutable
import scalafx.scene.text.Font
import scalafx.scene.text.TextAlignment
import cs2.util.Vec2
import cs2.game.Boss

/** main object that initiates the execution of the game, including construction
 *  of the window.
 *  Will create the stage, scene, and canvas to draw upon. Will likely contain or
 *  refer to an AnimationTimer to control the flow of the game.
 */
object SpaceGameApp extends JFXApp {
  stage = new JFXApp.PrimaryStage {
    title = "Space Game"
    //SCENE CREATION
    scene = new Scene(600,600) { //Scene size hardcoded
      //CANVAS CREATION
      val canvas = new Canvas(width.value,height.value)
      content = canvas
      val g = canvas.graphicsContext2D

      //MAKE BACKGROUND BLACK (METHOD AND CALL TO)
      def refresh():Unit = {
        g.setFill(Color.Black)
        g.fillRect(0,0, width.value,height.value)
      }
      refresh()

      //BULLET PIC INITIALIZATIONS (needed for player creation, enemy shots, boss shots)
      val pbulletPic = new Image(getClass().getResource("/images/PlayerBullet.png").toString) //Current image size ???
      val ebulletPic = new Image(getClass().getResource("/images/EnemyBullet.png").toString) //Current image size ???
      val bbulletPic = new Image(getClass().getResource("/images/BossBullet.png").toString)

      //ENEMY PIC INITIALIZATION (needed for enemy/enemyswarm creation)
      val enemyPic = new Image(getClass().getResource("/images/Enemy.png").toString) //Current image size ???

      //BOSS PIC INITIALIZATION (needed for boss creation)
      val bossPic = new Image(getClass().getResource("/images/Boss.png").toString)

      //BACKGROUND PIC INITIALIZATIONS (needed for parallax background componenets)
      val smallStarsPic = new Image(getClass().getResource("/images/smalleststars.png").toString)
      val medStarsPic = new Image(getClass().getResource("/images/biggerstars.png").toString)
      val bigStarsPic = new Image(getClass().getResource("/images/biggeststars.png").toString)
      val asteroidsPic = new Image(getClass().getResource("/images/asteroids.png").toString)
      val planetsPic = new Image(getClass().getResource("/images/planets.png").toString)
    
      //BACKGROUND INITIALIZATIONS (parallax background components)
      var backgrounds = Buffer[Background]() //buffer so can loop over, reset, step, and display components more easily
      //add two copies of each component, one "behind" the other to "cycle" through images
      //ordered fartherst to nearest
      backgrounds +:= new Background(smallStarsPic, Vec2(0,0), Vec2(0,-0.01)) //smallstars1
      backgrounds +:= new Background(smallStarsPic, Vec2(0,height.value), Vec2(0,-0.01)) //smallstars2
      backgrounds +:= new Background(planetsPic, Vec2(0,0), Vec2(0,-0.03)) //planets1
      backgrounds +:= new Background(planetsPic, Vec2(0,height.value), Vec2(0,-0.03)) //planets2
      backgrounds +:= new Background(medStarsPic, Vec2(0,0), Vec2(0,-0.07)) //medstars1
      backgrounds +:= new Background(medStarsPic, Vec2(0,height.value), Vec2(0,-0.07)) //medstars2
      backgrounds +:= new Background(asteroidsPic, Vec2(0,0), Vec2(0,-0.15)) //asteroids1
      backgrounds +:= new Background(asteroidsPic, Vec2(0,height.value), Vec2(0,-0.15)) //asteroids2
      backgrounds +:= new Background(bigStarsPic, Vec2(0,0), Vec2(0,-0.25)) //bigstars1
      backgrounds +:= new Background(bigStarsPic, Vec2(0,height.value), Vec2(0,-0.25)) //bigstars2

      //PLAYER CREATION
      val playerPic = new Image(getClass().getResource("/images/Player.png").toString) //Current img size ???
      //Current player init position determined by values in Vec2 labelled spawnX and spawnY
      val pspawnX = width.value/2 - playerPic.width.value/2
      val pspawnY = height.value*.85
      val p = new Player(playerPic, Vec2(pspawnX, pspawnY), pbulletPic)
      
      //INITIAL ENEMY SWARM CREATION
      val r = 2 //num rows
      val c = 4 //num cols
      var e = new EnemySwarm(enemyPic, ebulletPic, r, c, Vec2((width.value - enemyPic.width.value*c - pbulletPic.width.value*(c-1))/2,5)) //creates swarm, always spawn at top in center of screen
      
      //INITIAL BOSS "CREATION"
      var boss:Boss = null

      //QUEUE of BOSS MOVEMENTS
      var moves = mutable.Queue[mutable.Set[KeyCode]]()
      //SET of "CURRENT" BOSS MOVEMENTS
      var copycodes = mutable.Set[KeyCode]()

      //BULLETS
      var pbullets = Buffer[Bullet]() //player bullets
      var ebullets = Buffer[Bullet]() //enemy bullets
      var bullets = pbullets ++ ebullets

      //NOTE KEYS PRESSED (USER INPUT 1)
      val keycodes = mutable.Set[KeyCode]()

      canvas.onKeyPressed = (e:KeyEvent) => {
        keycodes += e.code
        //println(keycodes)
      } 
              
      canvas.onKeyReleased = (e:KeyEvent) => {
        keycodes -= e.code
        //println(keycodes)
      }

      //SHOT COOLDOWN VARIABLES (start at 0, so player/enemy can shoot immediately)
      var eCooldown = 0
      var pCooldown = 0
      
      //BOSS MOVEMENT COOLDOWN VARIABLE
      val delay = 20
      var bCooldown = delay

      //SCORE, STAGE, and LIVES TRACKING
      val numLives = 3 //num starting lives (also used to reset game)
      var stage = 1 //start at 1
      var score = 0 //start at 0
      var lives = numLives //start at numLives
      //println(score)
      //println(lives)

      //START SCREEN BOOLEAN
      var showStartScreen = true

      //GAME OVER BOOLEAN
      var gameOver = false

      //TRACK END SCORE VALUE
      var endScore = 0

      //ANIMATION TIMER START
      val timer = AnimationTimer (t => {
        //CHECK IF GAME ENDED, CHANGE BOOLEAN AND RESET IF SO
        if(lives == 0) {
          gameOver = true
          endScore = score //save end score so that it can be displayed accurately
          score = 0 //reset score
          stage = 1 //reset stage number
          lives = numLives //reset lives
          pbullets = Buffer[Bullet]() //reset player bullets
          ebullets = Buffer[Bullet]() //reset enemy bullets
          bullets = pbullets ++ ebullets //reset bullets
          e = new EnemySwarm(enemyPic, ebulletPic, r, c, Vec2((width.value - enemyPic.width.value*c - pbulletPic.width.value*(c-1))/2,5)) //reset enemy swarm
          boss = null //reset boss
          p.moveTo(Vec2(pspawnX, pspawnY)) //reset player pos
          eCooldown = 0 //reset enemy cooldown
          pCooldown = 0 //reset player cooldown
          bCooldown = delay
        }

        //EXECUTE KEYS PRESSED for GAME START/RETRY (USER INPUT 2)
        if(showStartScreen && keycodes.contains(KeyCode.Enter)) {
          showStartScreen = false
        }
        if(gameOver && keycodes.contains(KeyCode.Enter)) {
          gameOver = false
        }

        //START SCREEN DISPLAY
        if(showStartScreen) {
          refresh()
          g.setTextAlign(TextAlignment.Center)
          g.setFont(Font("Bauhaus 93", 100)) //ms word font, NOTE: (AS OF NOW) will use default if not found on user system
          g.setFill(Color.White)
          g.fillText("SPACE GAME", width.value/2,height.value/2, width.value)
          g.setFont(Font("Courier New", 15)) //ms word font
          g.fillText("press ENTER to start", width.value/2,height.value/2+50, width.value)
        } else if(gameOver) { //END SCREEN DISPLAY
          refresh()
          g.setTextAlign(TextAlignment.Center)
          g.setFont(Font("Bauhaus 93", 100)) //ms word font
          g.setFill(Color.White)
          g.fillText("GAME OVER", width.value/2,height.value/2, width.value)
          g.setFont(Font("Courier New", 15)) //ms word font
          g.fillText("score: " + endScore, width.value/2,height.value/2+50, width.value)
          g.fillText("press ENTER to play again", width.value/2,height.value/2+100, width.value)
        } else {

          refresh() //refreshes screen at the beginning of each new frame

          //BACKGROUND DISPLAY and MOVEMENT
          for(b <- backgrounds) {
            b.display(g)
            b.timeStep()
            if(b.position().y <= -1*canvas.height.value) {
              b.moveTo(Vec2(0,canvas.height.value))
            }
          }

          //PLAYER and ENEMY DISPLAY
          p.display(g) //displays updated position of player
          if(!e.isEmpty() && stage % 2 != 0) {
            e.display(g) //displays enemy swarm
          } else if(boss != null && stage % 2 == 0) {
            boss.display(g) //displays boss
          }

          //BULLET DISPLAY and MOVEMENT
          for(b <- bullets) { //note that value of bullets is refreshed at end of animationtimer
            b.display(g) //display current bullets
            b.timeStep() //move current bullets
          }

          //UI DISPLAY
          g.setTextAlign(TextAlignment.Left)
          g.setFill(Color.White)
          g.setFont(Font("Courier New", 15)) //ms word font
          g.fillText("lives: " + lives, 5,height.value-5, width.value)
          g.fillText("score: " + score, 105,height.value-5, width.value) //NOTE THAT X VAL IS HARDCODED

          //ENEMY SHOOTING
          if(!(e.isEmpty()) && Random.nextInt(200) == 0 && eCooldown <= 0) { //may want to change to be based on time between shots
            ebullets +:= e.shoot()
            eCooldown = 50 //ENEMY COOLDOWN
          } 
          eCooldown -= 1
          
          //ENEMY MOVEMENT
          e.move(canvas)
          
          if(boss != null && stage % 2 == 0) {
            moves.enqueue(keycodes.clone())//copying set of keycodes for boss movements
            bCooldown -= 1
            if(bCooldown <= 0) { //boss movement
              copycodes = moves.dequeue()
              //EXECUTE KEYS PRESSED for BOSS MOVEMENT (USER INPUT 3)
              if((copycodes.contains(KeyCode.Left) || keycodes.contains(KeyCode.A)) && boss.position.x > 0 ) {
                boss.moveLeft()
              }
              if((copycodes.contains(KeyCode.Right) || keycodes.contains(KeyCode.D)) && boss.position.x + bossPic.width.value < width.value) {
                boss.moveRight()
              }
              if((copycodes.contains(KeyCode.Up) || keycodes.contains(KeyCode.W)) && boss.position.y + bossPic.height.value < height.value) {
                boss.moveDown()
                //println(p.position())
              }
              if((copycodes.contains(KeyCode.Down) || keycodes.contains(KeyCode.S)) && boss.position.y > 0) {
                boss.moveUp()
              }
              if(copycodes.contains(KeyCode.Space) && eCooldown <= 0) { //BOSS SHOOTING
                ebullets +:= boss.shoot()
                eCooldown = 50
              }
            }
          }

          //EXECUTE KEYS PRESSED for PLAYER MOVEMENT (USER INPUT 4)
          if((keycodes.contains(KeyCode.Left) || keycodes.contains(KeyCode.A)) && p.position.x > 0 ) {
            p.moveLeft()
          }
          if((keycodes.contains(KeyCode.Right) || keycodes.contains(KeyCode.D)) && p.position.x + playerPic.width.value < width.value) {
            p.moveRight()
          }
          if((keycodes.contains(KeyCode.Up) || keycodes.contains(KeyCode.W)) && p.position.y > 0) {
            p.moveUp()
            //println(p.position())
          }
          if((keycodes.contains(KeyCode.Down) || keycodes.contains(KeyCode.S)) && p.position.y + playerPic.height.value < height.value) {
            p.moveDown()
          }
          if(keycodes.contains(KeyCode.Space) && pCooldown <= 0) { //PLAYER SHOOTING
            pbullets +:= p.shoot()
            pCooldown = 50 //PLAYER COOLDOWN
          }
          pCooldown -= 1

          //CHECKING FOR INTERSECTIONS

          //intersections between player and enemies
          if(e.intersects(p) || (boss != null && boss.intersects(p))) {
            p.moveTo(Vec2(pspawnX, pspawnY))
            lives -= 1
            //println(lives)
          }

          //intersections between enemy bullets and player
          var remove = Buffer[Bullet]()
          for(eb <- ebullets) {
            if(eb.intersects(p)) {
              p.moveTo(Vec2(pspawnX, pspawnY))
              remove += eb
              lives -= 1
              //println(lives)
            }
          }
          ebullets --= remove

          //intersections between enemy bullets and player bullets
          var eremove = Buffer[Bullet]()
          var premove = Buffer[Bullet]()
          for(eb <- ebullets) {
            for(pb <- pbullets) {
              if(eb.intersects(pb)) {
                eremove += eb
                premove += pb
              }
            }
          }
          ebullets --= eremove
          pbullets --= premove
          
          //intersections between enemies and player bullets
          premove = Buffer[Bullet]()
          for(pb <- pbullets) {
            if(e.hitBy(pb)) { //hitBy performs removal of enemies in EnemySwarm.scala
              premove += pb
              score += 100 
              //println(score)
            } else if(boss != null && boss.intersects(pb)) {
              premove += pb
              boss.hp -= 1
            }
          }
          pbullets --= premove

          //CHANGING STAGE NUMBER
          if((stage % 2 != 0 && e.isEmpty()) || (stage % 2 == 0 && boss == null)) {
            stage += 1
            //println(stage)
          }

          //"RECREATING" ENEMYSWARM IF LAST IS EMPTY and IS APPROPRIATE STAGE NUMBER
          if(e.isEmpty() && stage % 2 != 0) {
            e = new EnemySwarm(enemyPic, ebulletPic, r, c, Vec2((width.value - enemyPic.width.value*c - pbulletPic.width.value*(c-1))/2,5)) //reset enemy swarm
          }

          //"RECREATING" BOSS IF LAST IS NULL and IS APPROPRIATE STAGE NUMBER
          if(boss == null && stage % 2 == 0) {
            boss = new Boss(bossPic, Vec2(((width.value - bossPic.width.value)/2), 25), bbulletPic, 5)
            bCooldown = delay
          }

          //REMOVING BOSS IF 0 HP and INCREASING SCORE
          if(boss != null && boss.hp == 0) {
            boss = null
            score += 1000
          }
          
          //CLEANING UP OFFSCREEN BULLETS
          pbullets = pbullets.filter((b:Bullet) => b.isOnscreen(canvas))
          ebullets = ebullets.filter((b:Bullet) => b.isOnscreen(canvas))

          //REFRESH VALUE OF BULLETS
          bullets = ebullets ++ pbullets
          //println(bullets.length) - use to test if filter is working

        }
      })
      //ANIMATION TIMER END
      

      canvas.requestFocus() //requests focus for keyboard/mouse input
      timer.start() //starts animation timer

    }
  }
}
