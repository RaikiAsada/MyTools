# frozen_string_literal: true
require 'rexml/document'

class Main
  def main(rootDir, jobName)
    jobPath = "#{rootDir}\\#{jobName}"

    if !Dir.exist? jobPath
      puts "指定したジョブは存在しません:#{jobName}"
      return
    end

    successBrunches = []

    Dir.chdir jobPath
    Dir.glob('*').sort_by{|d| File.birthtime(d)}.each do |taskDir|
      taskPath = "#{jobPath}\\#{taskDir}"

      brunch = readBuildBrunch(taskPath)

      if(isSuccess(taskPath))
        successBrunches << brunch
      else
        if(successBrunches.include?(brunch))
          successBrunches.delete(brunch)
        end
      end
    end

    successBrunches.uniq.each do |brunch|
      puts(brunch)
    end
  end

  def readBuildBrunch(taskPath)
    Dir.chdir taskPath
    doc = REXML::Document.new File.new("build.xml")
    return doc.elements['build/actions/hudson.model.ParametersAction/parameters/net.uaznia.lukanus.hudson.plugins.gitparameter.GitParameterValue/value'].text
  end

  def isSuccess(taskPath)
    result = readTaskResult taskPath
    return result.eql? 'SUCCESS'
  end

  def readTaskResult(taskPath)
    Dir.chdir taskPath
    doc = REXML::Document.new File.new "build.xml"
    return doc.elements['build/result'].text
  end
end

Main.new.main ARGV[0], ARGV[1]
