package com.jblearning.duckhuntingv3;

import android.graphics.Point;
import android.graphics.Rect;

import java.util.Random;

public class Game {
  private Rect huntingRect;
  private int deltaTime; // in milliSeconds
  
  private Rect[] duckRect = new Rect[2];
  private int duckWidth; 
  private int duckHeight; 
  private float duckSpeed;
  private boolean duckShot;
  
  private Point cannonCenter; 
  private int cannonRadius;
  private int barrelLength;
  private int barrelRadius;
  private float cannonAngle;

  private Point bulletCenter; 
  private int bulletRadius; 
  private boolean bulletFired;
  private float bulletAngle; 
  private float bulletSpeed;
  
  private Random random;
  
  public Game( Rect[] newDuckRect, int newBulletRadius,
               float newDuckSpeed, float newBulletSpeed ) {
    setDuckRect( newDuckRect );
    setDuckSpeed( newDuckSpeed );
    setBulletRadius( newBulletRadius );
    setBulletSpeed( newBulletSpeed ); 
    random = new Random( );
    bulletFired = false;
    duckShot = false;
    cannonAngle = ( float ) Math.PI / 4; // starting cannon angle
  }
 
  public Rect getHuntingRect( ) {
    return huntingRect;
  }  

  public void setHuntingRect( Rect newHuntingRect ) {
	if( newHuntingRect != null )
      huntingRect = newHuntingRect;
  }
  
  public void setDeltaTime( int newDeltaTime ) {
    if( newDeltaTime > 0 )
      deltaTime = newDeltaTime;
  }
  
  public Rect[] getDuckRect( ) {
      return duckRect;
//    return new Rect[]{duckRect[1]};
  }

  public Rect[] getDuckRect2( ) {
    return new Rect[]{duckRect[2]};
  }

  public void setDuckRect( Rect[] newDuckRect ) {
    if( newDuckRect != null ) {
      duckWidth = newDuckRect[1].right - newDuckRect[1].left;
      duckHeight = newDuckRect[1].bottom - newDuckRect[1].top;
      duckRect = newDuckRect;
    }
  }
  
  public void setDuckSpeed( float newDuckSpeed ) {
    if( newDuckSpeed > 0 )
      duckSpeed = newDuckSpeed;
  }

  public Point getCannonCenter( ) {
    return cannonCenter;
  }
  
  public int getCannonRadius( ) {
    return cannonRadius;
  }
  
  public int getBarrelLength( ) {
    return barrelLength;
  }
  
  public int getBarrelRadius( ) {
    return barrelRadius;
  }
  
  public void setCannon( Point newCannonCenter, int newCannonRadius, 
                         int newBarrelLength, int newBarrelRadius ) {
    if( newCannonCenter != null && newCannonRadius > 0 
    		                    && newBarrelLength > 0 ) {
      cannonCenter = newCannonCenter;
      cannonRadius = newCannonRadius;
      barrelLength = newBarrelLength;
      barrelRadius = newBarrelRadius;
      bulletCenter = new Point( 
      ( int ) ( cannonCenter.x + cannonRadius * Math.cos( cannonAngle ) ), 
      ( int ) ( cannonCenter.y - cannonRadius * Math.sin( cannonAngle ) ) );
    }
  }
  
  public Point getBulletCenter( ) {
    return bulletCenter;
  }
  
  public int getBulletRadius( ) {
    return bulletRadius;
  }

  public void setBulletRadius( int newBulletRadius ) {
    if( newBulletRadius > 0 )
      bulletRadius = newBulletRadius;
  }
  
  public void setBulletSpeed( float newBulletSpeed ) {
    if( newBulletSpeed > 0 )
      bulletSpeed = newBulletSpeed;
  }
  
  public float getCannonAngle( ) {
    return cannonAngle;
  }

  public void setCannonAngle( float newCannonAngle ) {
    if( newCannonAngle >= 0 && newCannonAngle <= Math.PI / 2 )
      cannonAngle = newCannonAngle;
    else if( newCannonAngle < 0 )
      cannonAngle = 0;
    else
      cannonAngle = ( float ) Math.PI / 2;
    if( !isBulletFired( ) )
      loadBullet( );
  }
  
  public boolean isBulletFired( ) {
    return bulletFired;
  }
  
  public void fireBullet( ) {
    bulletFired = true;
    bulletAngle = cannonAngle;
  }
  
  public boolean isDuckShot( ) {
    return duckShot;
  }   
  
  public void setDuckShot( boolean newDuckShot ) {
    duckShot = newDuckShot;
  }
   
  public void startDuckFromRightTopHalf( ) {

      duckRect[1].left = huntingRect.right;
    duckRect[1].right = duckRect[1].left + duckWidth;
    duckRect[1].top = random.nextInt( huntingRect.bottom / 2 );
    duckRect[1].bottom = duckRect[1].top + duckHeight;


      duckRect[2].left = huntingRect.right;
    duckRect[2].right = duckRect[2].left + duckWidth;
    duckRect[2].top = random.nextInt( huntingRect.bottom / 2 );
    duckRect[2].bottom = duckRect[2].top + duckHeight;
  }
  
  public void moveDuck( ) {
    if( !duckShot ) { // move left 
      duckRect[1].left -= duckSpeed * deltaTime;
      duckRect[1].right -= duckSpeed * deltaTime;
    } else { // move down
      duckRect[1].top += 5 * duckSpeed * deltaTime;
      duckRect[1].bottom += 5 * duckSpeed * deltaTime;
    }

    if( !duckShot ) { // move left
      duckRect[2].left -= duckSpeed * deltaTime;
      duckRect[2].right -= duckSpeed * deltaTime;
    } else { // move down
      duckRect[2].top += 5 * duckSpeed * deltaTime;
      duckRect[2].bottom += 5 * duckSpeed * deltaTime;
    }
  }
  
  public boolean duckOffScreen( ) {
    return duckRect[1].right < 0 || duckRect[1].bottom < 0
        || duckRect[1].top > huntingRect.bottom
        || duckRect[1].left > huntingRect.right ||
        duckRect[2].right < 0 || duckRect[2].bottom < 0
        || duckRect[2].top > huntingRect.bottom
        || duckRect[2].left > huntingRect.right;
  }
  
  public void moveBullet( ) {
    bulletCenter.x += bulletSpeed * Math.cos( bulletAngle ) * deltaTime;
    bulletCenter.y -= bulletSpeed * Math.sin( bulletAngle ) * deltaTime;
  }
  
  public boolean bulletOffScreen( ) {
    return bulletCenter.x - bulletRadius > huntingRect.right 
        || bulletCenter.y + bulletRadius < 0;
  }
  
  public void loadBullet( ) {
    bulletFired = false;
    bulletCenter.x = ( int ) ( cannonCenter.x 
      + cannonRadius * Math.cos( cannonAngle ) );
    bulletCenter.y = ( int ) ( cannonCenter.y 
      - cannonRadius * Math.sin( cannonAngle ) );
  }

  public boolean duckHit( ) {  
    return duckRect[1].intersects(
      bulletCenter.x - bulletRadius, bulletCenter.y - bulletRadius, 
      bulletCenter.x + bulletRadius, bulletCenter.y + bulletRadius ) ||
      duckRect[2].intersects(
      bulletCenter.x - bulletRadius, bulletCenter.y - bulletRadius,
      bulletCenter.x + bulletRadius, bulletCenter.y + bulletRadius );
  }
}