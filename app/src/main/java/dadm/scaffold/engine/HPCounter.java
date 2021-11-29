package dadm.scaffold.engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class HPCounter extends GameObject {

    private final float textWidth;
    private final float textHeight;

    private Paint paint;
    private long totalMillis;
    private int draws;
    public static int HP;

    private String HPText = "";

    public HPCounter(GameEngine gameEngine) {
        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        textHeight = (float) (25 * gameEngine.pixelFactor);
        textWidth = (float) (50 * gameEngine.pixelFactor);
        paint.setTextSize(textHeight / 2);
    }

    @Override
    public void startGame() {
        totalMillis = 0;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        totalMillis += elapsedMillis;
        if (totalMillis > 100) {
            HPText = (HP+1)+ " Lifes";
            totalMillis = 0;
            draws = 0;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.TRANSPARENT);
        canvas.drawRect(200, 0, textWidth, textHeight, paint);
        paint.setColor(Color.WHITE);
        canvas.drawText(HPText, textWidth+200, textHeight, paint);
        draws++;
    }
}
