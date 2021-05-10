package com.hllbr.fruitninjastarter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

public class FruitNinja extends ApplicationAdapter implements InputProcessor {
	ShapeRenderer shapes;
	SpriteBatch batch;

	Texture background ;


	//other textures
	Texture apple ;
	Texture bill ;//para
	Texture cherry ;
	Texture ruby ;

	Random random = new Random();
	Array<Fruit> fruitArray = new Array<Fruit>();
	//Çok fazla Bitmap font kulalndığında pixelleşme sorunlarıyla karşılalabilirsin bu sebeple bir alternatif yöntem kullanmaya çalışacağım .

	BitmapFont font ;
	FreeTypeFontGenerator fontGen;


	int lives = 0 ;
	int score = 0 ;

	float genCounter = 0;
	private final  float startGenSpeed = 1.1f;
	float genSpeed = startGenSpeed;

	//Oyun içerisinde Zamanı kontrol eden mekanizmalara ihtiyacım oluyor şimdi bunları oluşturuyorum

	private double currentTime;//Güncel Zaman
	private double gameOverTime = -1.f;//Oyunun bittiği zamanı alıyorum 0 dan küçük bir değer ile başlatmayı uygun buluyorum Başlangıçta




	@Override
	public void create () {
		shapes = new ShapeRenderer();
		batch = new SpriteBatch();
		background = new Texture("ninjabackground.png");

		//initialize operations(PNG)

		apple = new Texture("apple.png");
		bill = new Texture("bill.png");
		cherry = new Texture("cherry.png");
		ruby = new Texture("ruby.png");

		//GDX Kullanıcının tıklamalarını kaydırmalarını ... algılamak için bir input prosedürü kulalnıyor .Kullanıcının inputlarını algulayacak bir prosesor bulunduruyor
		//Bunun için bir arayüz procesor arayüzünü implement ederek bu yapıyı işleme dahil etmiş oluyorum

		Fruit.radius = Math.max(Gdx.graphics.getHeight(),Gdx.graphics.getWidth())/(2f*10f);
		//Create içerisinde bu kullanıcı inputlarını alacak bunu belirlemek gerekiyor onun için alt satırdaki kod yapısı uygun bir tanımlamadır

		Gdx.input.setInputProcessor(this);	//Buradaki Activity'e referans veriyorum

		//Font Settings Operation

		fontGen = new FreeTypeFontGenerator(Gdx.files.internal("robotobold.ttf"));//Burada hangi fontu kullanacağımın parametresini istiyor bende pasrametre olarak
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();//Bu yapı sayesinde yeni parametreler oluşturabilirim.
		params.color = Color.BLACK;//paramter. dediğimde fonla ilgili şeyleri değiştirebiliyorum
		params.size = 55;//px
		params.characters = "0123456789 ScreCutoplay!:,+-";
		font = fontGen.generateFont(params);

	}


	@Override
	public void render () {//Oyunun bitip bitmedğini bu alan içersinde kontrol etmem ve buna göre sürecin devamlılığına karar vermem gerekiyor.
		//Her durumda göstermek istediğim ifadelerim var örneğin arkaplan her zaman gösterilmesini istiyorum

		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		double newTime = TimeUtils.millis()/1000.0;
		System.out.println("new Time : "+newTime);//Time Testing

		double frameTime = Math.min(newTime - currentTime,0.3);
		System.out.println("FrameTime :"+frameTime);
		float deltaTime =(float) frameTime;
		System.out.println("DeltaTime : "+deltaTime);
		currentTime = newTime;//Şuana kadar sadece zamanı kontrol eden mekanizma oluştu benim birde meyvelerin oluşturlma hızını kontrol eden bir mekanizma inşa etmem gerekiyor.


		/*
		Render zamanı telefona işlemciye göre değişebilir.BAZI DURUMLARDA 1 SANİYE İÇERİSİNDE 60 FRAME GÖRÜYORKEN BAZI durumlarda 1 saniede daha fazla yada daha az olabilir.
		Farklı telefonlarda aynı zamanı sonuçlara ulaşabilmek için FrameTime bilmemiz gerekiyor.

		*/


		if(lives <= 0 && gameOverTime == 0f){
			/*oyuncunun oynama hakkı kalmadı ve oyun zamanı 0f olduğunda ...
			GameOver Screen operations
			Oyunun bittiği zamanı burada kaydetmek istiyorum
			Güncel zamanı oynadığım ekranın boyutlarından çıkarmaya çalışcağım


			 */
			gameOverTime = currentTime;

		}
		if(lives > 0){
			//Eğer Oyun hala devam ediyorsa ...
			//Game Mode
			System.out.println("radius : "+Fruit.radius);
			genSpeed -= deltaTime*0.015f;
			System.out.println("genSpeed : "+genSpeed);
			System.out.println("genCounter : "+genCounter);

			if(genCounter <= 0f){
				genCounter = genSpeed;
				addItem();
			}else{
				genCounter -= deltaTime;
			}

			for(int i= 0;i<lives;i++){
				batch.draw(apple,i*30f+25f,Gdx.graphics.getHeight()-25f,25f,25f);//4 elmayı yan yana göstermek için kullandığım yapı
				//batch.draw(apple,30f,Gdx.graphics.getHeight()-25f,25f,25f);1 elma gösteriyor fakat üst üste 4 elmam oluyor bu şekilde.
			}

			for(Fruit fruit: fruitArray){
				fruit.update(deltaTime);

				switch (fruit.type){
					case  REGULAR:
						batch.draw(apple,fruit.getPos().x,fruit.getPos().y,Fruit.radius,Fruit.radius);
						break;
					case EXTRA:
						batch.draw(cherry,fruit.getPos().x,fruit.getPos().y,Fruit.radius,Fruit.radius);
						break;
					case ENEMY:
						batch.draw(ruby,fruit.getPos().x,fruit.getPos().y,Fruit.radius,Fruit.radius);
						break;
					case LIFE:
						batch.draw(bill,fruit.getPos().x,fruit.getPos().y,Fruit.radius,Fruit.radius);
						break;
				}
			}


			boolean holdLives = false;
			Array<Fruit> toRemove = new Array<Fruit>();

			for(Fruit fruit : fruitArray){
				if(fruit.outOfScreen()){
					toRemove.add(fruit);
					if(fruit.living && fruit.type == Fruit.Type.REGULAR){
						lives--;
						holdLives = true;
						break;
					}
				}
			}
			if(holdLives){
				for(Fruit f: fruitArray){
					f.living = false;
				}
			}
			for(Fruit f : toRemove){
				fruitArray.removeValue(f,true);

			}

		}

		font.draw(batch,"Score: "+score,30,40);
		if(lives<=0){
			font.draw(batch,"Cut to play!",Gdx.graphics.getWidth()*0.5f,Gdx.graphics.getHeight()*0.5f);

		}
		batch.end();
	}
	private void addItem(){
		//float pos = random.nextFloat()*Math.max(Gdx.graphics.getHeight(),Gdx.graphics.getWidth());
		float pos = random.nextFloat()*Gdx.graphics.getWidth();
		Fruit item = new Fruit(new Vector2(pos,-Fruit.radius),new Vector2((Gdx.graphics.getWidth()*0.5f-pos)*0.3f+(random.nextFloat()-0.5f),Gdx.graphics.getHeight()*0.5f));

		float type = random.nextFloat();
		if(type>0.98){
			item.type =Fruit.Type.LIFE;
		}else if(type > 0.88){
			item.type = Fruit.Type.EXTRA;
		}else if(type > 0.78){
			item.type =Fruit.Type.ENEMY;
		}
		fruitArray.add(item);
	}
	
	@Override
	public void dispose () {
		batch.dispose();//Kullandığımız yapıalrı bu alanda disponse edilmesi gerekiyor.
		shapes.dispose();
		font.dispose();
		fontGen.dispose();

	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		//Kullanıcı dokundu ve sürükledi benim daha çok kullanacağım yapı burada bulunuyor istediğim işlemler dizisini bu alana yazıyorum .Çünkü bu bir mobil app....
		if(lives <= 0 && currentTime -gameOverTime>2f){
			//oyun yeni başlıyor ** Menu Mode

			gameOverTime = 0f;
			score = 0;
			lives = 4;//restart game
			genSpeed = startGenSpeed;
			fruitArray.clear();
		}else {
			//Oyun Devam ediyorsa ** Game Mode
			//Çıkartılacaklar dizisi oluşturmam gerekiyor eğer kullanıcı meyvenin üzerine elini dokundurmayı başardıysa o meyveyi çıkarmam gerekiyor.
			Array<Fruit> toRemove = new Array<Fruit>();
			Vector2 pos = new Vector2(screenX, Gdx.graphics.getHeight()-screenY);
			int plusScore = 0;
			for (Fruit f : fruitArray) {
				System.out.println("getHeigth : "+screenY);
				System.out.println("getHeight : "+(Gdx.graphics.getHeight()-screenY));
				System.out.println("getHeight : "+f.getPos());
				System.out.println("distance : "+pos.dst2(f.pos));
				System.out.println("distance : "+f.clicked(pos));
				System.out.println("distance : "+Fruit.radius*Fruit.radius+1);
				if (f.clicked(pos)) {
					toRemove.add(f);
					switch (f.type) {
						case REGULAR:
							plusScore ++;
							break;
						case EXTRA:
							plusScore += 2;
							score++;
							break;
						case ENEMY:
							lives--;
							break;
						case LIFE:
							lives++;
							break;

					}

				}
			}
			score +=plusScore*plusScore;

			for(Fruit f : toRemove){
				fruitArray.removeValue(f,true);

			}
		}

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}
}
