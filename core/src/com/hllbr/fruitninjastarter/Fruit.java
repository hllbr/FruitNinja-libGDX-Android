package com.hllbr.fruitninjastarter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Fruit {
    //Meyvelerin yarıçapını boyutlarını oluşturabilmek için bir değşkene ihtiyacım var
    public static float radius = 60f;

    public enum Type{//Enum bizim için farklı sınıflandırmalar oluşturuyor.
        REGULAR,EXTRA,ENEMY,LIFE
    }
    Type type;
    Vector2 pos,velocity;
    Fruit(Vector2 pos,Vector2 velocity){
        this.pos = pos;
        this.velocity = velocity;
        type =Type.REGULAR;
    }
    public boolean living = true;
    
    //Bir meyveye tıklanıp tıklanılmadğığnı kontrol etmk gerekiyor bunun için bir ytapı kurmam gerekiyor.
    public boolean clicked(Vector2 click){
        if(pos.dst2(click)<= radius*radius+1) return true;
        return false;
    }
    public final Vector2 getPos(){
        //Meyvanin güncel pozisyonunu almak için kullandığım bir yapı
        return pos;

    }
    public boolean outOfScreen(){
        //Meyvenin ekran dışına çıkıp çıkmadığının kontrolünü yapmam gerekiyor can durumunu oyunun devamlılığını kontrol etmek için
        return (pos.y<-2f*radius);
    }
    public void update(float dt){
        //hız ve pozisyonun sürekli güncellenmesi gerekiyor.Bunuda FruitNina içerisinde oluşturduğum deltaTime'a göre yapmak istiyorum
        //Critic area

        velocity.y -= dt*(Gdx.graphics.getHeight() * 0.2f);
        velocity.x -= dt*Math.signum(velocity.x)*5f;
        pos.mulAdd(velocity,dt);//multiplayeAdd gibi istediğin vectörü güncelleme şansı veren bir metod
    }
}
