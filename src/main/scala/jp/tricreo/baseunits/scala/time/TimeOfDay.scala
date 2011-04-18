package jp.tricreo.baseunits.scala.time

import java.util.TimeZone

/**
 * 1日の中の特定の「時分」を表すクラス。
 *
 * <p>[[java.util.Date]]と異なり、日付の概念を持っていない。またタイムゾーンの概念もない。</p>
 * @param hour 時
 * @param minute 分
 */
@serializable
class TimeOfDay
(private val hour: HourOfDay,
 private val minute: MinuteOfHour) extends Ordered[TimeOfDay] {


  /**
   * 指定した年月日とタイムゾーンにおける、このインスタンスがあらわす時分の0秒0ミリ秒の瞬間について {@link TimePoint} 型のインスタンスを返す。
   *
   * @param date 年月日
   * @param timeZone タイムゾーン
   * @return 瞬間
   * @throws IllegalArgumentException 引数に{@code null}を与えた場合
   */
  def asTimePointGiven(date: CalendarDate, timeZone: TimeZone): TimePoint = {
    val timeOfDayOnDate = on(date)
    timeOfDayOnDate.asTimePoint(timeZone)
  }

  /**
   * このオブジェクトの{@link #hour}フィールド（時）を返す。
   *
   * <p>CAUTION: このメソッドは、このオブジェクトがカプセル化する要素を外部に暴露する。取り扱いには充分注意のこと。</p>
   *
   * @return 時
   */
  def breachEncapsulationOfHour = hour


  /**
   * このオブジェクトの{@link #minute}フィールド（分）を返す。
   *
   * <p>CAUTION: このメソッドは、このオブジェクトがカプセル化する要素を外部に暴露する。取り扱いには充分注意のこと。</p>
   *
   * @return 分
   */
  def breachEncapsulationOfMinute = minute;


  def compare(other: TimeOfDay): Int = {
    val hourComparance = hour.compareTo(other.hour)
    if (hourComparance != 0) hourComparance
    else minute.compareTo(other.minute)
  }

  override def equals(obj: Any): Boolean = obj match {
    case that: TimeOfDay => hour == that.hour && minute == that.minute
    case _ => false
  }

  override def hashCode = hour.hashCode + minute.hashCode


  /**
   * このインスタンスがあらわす時分が、指定した時分よりも未来であるかどうか調べる。
   *
   * <p>等価の場合は{@code false}を返す。</p>
   *
   * @param another 基準時分
   * @return 未来である場合は{@code true}、そうでない場合は{@code false}
   * @throws IllegalArgumentException 引数に{@code null}を与えた場合
   */
  def isAfter(another: TimeOfDay) = {
    hour.isAfter(another.hour) || (hour == another.hour && minute.isAfter(another.minute))
  }

  /**
   * このインスタンスがあらわす時分が、指定した時分よりも過去であるかどうか調べる。
   *
   * <p>等価の場合は{@code false}を返す。</p>
   *
   * @param another 基準時分
   * @return 過去である場合は{@code true}、そうでない場合は{@code false}
   * @throws IllegalArgumentException 引数に{@code null}を与えた場合
   */
  def isBefore(another: TimeOfDay) = {
    hour.isBefore(another.hour) || (hour == another.hour && minute.isBefore(another.minute))
  }

  /**
   * 指定した年月日における、このインスタンスがあらわす時分について {@link CalendarMinute} 型のインスタンスを返す。
   *
   * @param date 年月日
   * @return {@link CalendarMinute}
   * @throws IllegalArgumentException 引数に{@code null}を与えた場合
   */
  def on(date: CalendarDate) =
    CalendarMinute.from(date, this)

  override def toString = hour.toString + ":" + minute.toString

}

object TimeOfDay {

  /**インスタンスを生成する。
   *
   * @param hour 時
   * @param minute 分
   * @throws IllegalArgumentException 引数に{@code null}を与えた場合
   */
  def apply(hour: HourOfDay, minute: MinuteOfHour): TimeOfDay = new TimeOfDay(hour, minute)

  def unapply(timeOfDay: TimeOfDay) = Some(timeOfDay.hour, timeOfDay.minute)


  /**指定した時分を表す、{@link TimeOfDay}のインスタンスを生成する。
   *
   * @param hour 時
   * @param minute 分
   * @return {@link TimeOfDay}
   * @throws IllegalArgumentException 引数に{@code null}を与えた場合
   */
  def from(hour: HourOfDay, minute: MinuteOfHour): TimeOfDay = apply(hour, minute)

  /**
   * 指定した時分を表す、{@link TimeOfDay}のインスタンスを生成する。
   *
   * @param hour 時をあらわす正数（0〜23）
   * @param minute 分をあらわす正数（0〜59）
   * @return {@link TimeOfDay}
   * @throws IllegalArgumentException 引数{@code hour}が0〜23の範囲ではない場合
   * @throws IllegalArgumentException 引数{@code minute}が0〜59の範囲ではない場合
   */
  def from(hour: Int, minute: Int): TimeOfDay = new TimeOfDay(HourOfDay(hour), MinuteOfHour(minute))

}